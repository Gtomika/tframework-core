package org.tframework.core.ioc;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.tframework.core.ApplicationContext;
import org.tframework.core.ioc.annotations.Injected;
import org.tframework.core.ioc.annotations.Managed;
import org.tframework.core.ioc.constants.ConstructionMethod;
import org.tframework.core.ioc.constants.InjectionType;
import org.tframework.core.ioc.containers.AbstractContainer;
import org.tframework.core.ioc.exceptions.InvalidDependencyException;
import org.tframework.core.ioc.exceptions.NoSuchManagedEntityException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * Investigates the managed entities and resolves the dependencies between them by creating
 * and injecting instances of managed entities.
 */
@Slf4j
public class DependencyResolver {

    /**
     * A directed graph containing the dependency relations. If entity 'A' depends on entity 'B', then
     * there will be two nodes, 'A' and 'B', and there will be a directed edge pointing from 'B' to 'A'.
     * <p>
     * Initialized with {@link #discoverDependencies()}.
     */
    private final MutableGraph<String> dependencyGraph;

    /**
     * Managed entity repository, which should already be initialized by the time the dependency resolver is created.
     */
    private final ManagedEntitiesRepository repository;

    /**
     * Create a dependency resolver with an empty dependency graph.
     */
    protected DependencyResolver() {
        dependencyGraph = GraphBuilder.directed()
                .allowsSelfLoops(false)
                .build();
        repository = ApplicationContext.getInstance().getTFrameworkIoc().getManagedEntitiesRepository();
    }

    /**
     * Investigates the managed entities and finds the dependencies for each of them. These will be
     * saved as {@link DependencyInformation} objects in the containers of the managed entities.
     * <p>
     * Note that this does not resolve the dependencies, only discovers them. For example if managed entity 'A'
     * has an {@link org.tframework.core.ioc.annotations.Injected} field of managed entity 'B', then this dependency
     * will be discovered and saved in the container of 'A', and also in the {@link #dependencyGraph}.
     * @throws InvalidDependencyException If the dependencies could not be discovered or an illegal relation was found between them.
     * The cause exception contains the details.
     */
    public void discoverDependencies() throws InvalidDependencyException {
        for(AbstractContainer<?> container: repository.iterateManagedEntities()) {
            discoverDependenciesOfEntity(container);
        }
        checkCompletedDependencyGraph();
        log.info("Dependency relations between managed entities have been discovered, and are in order.");
    }

    /**
     * Discovers the dependencies of one managed entity. They will be added to the {@code container}.
     * @param container The {@link AbstractContainer} of the entity.
     * @throws InvalidDependencyException If there is an illegal dependency relation.
     */
    private void discoverDependenciesOfEntity(AbstractContainer<?> container) throws InvalidDependencyException {
        discoverFieldInjectionDependencies(container);
        discoverProviderMethodDependencies(container);
        discoverConstructorDependencies(container);
    }

    /**
     * Discovers all dependencies that are present through field injection, where a field is annotated with {@link Injected}.
     * @param container The container.
     * @throws InvalidDependencyException If there is an illegal dependency relation or invalid field annotated.
     */
    private void discoverFieldInjectionDependencies(AbstractContainer<?> container) throws InvalidDependencyException {
        List<Field> injectedFields = FieldUtils.getFieldsListWithAnnotation(container.getInstanceType(), Injected.class);
        log.debug("Found {} fields in managed entity '{}' annotated with @Injected", injectedFields.size(), container.getName());
        if(!injectedFields.isEmpty()) {
            //this entity has at least some dependencies
            addManagedEntityToGraph(container.getName());
        }
        for(Field injectedField: injectedFields) {
            log.debug("Checking the @Injected annotated field '{}' of managed entity '{}'", injectedField.getName(), container.getName());
            Injected injectedAnnotation = injectedField.getAnnotation(Injected.class);
            String dependencyName = getDependencyEntityName(injectedField, injectedAnnotation);
            try {
                IocValidator.validateInjectedField(injectedField);
                var dependencyContainer = repository.grabManagedEntityContainer(dependencyName);
                log.debug("This dependency references the following managed entity: '{}'", dependencyName);
                if(isSelfDependency(container.getName(), dependencyName)) {
                    throw new InvalidDependencyException(container.getName(), dependencyName, "Cannot depend on itself.");
                }
                //save in the graph
                addDependencyRelationToGraph(container.getName(), dependencyName);
                //save in dependencies of the container
                container.addDependency(
                        DependencyInformation.builder()
                                .dependencyContainer(dependencyContainer)
                                .injectionType(InjectionType.FIELD_INJECTION)
                                .injectedField(injectedField)
                                .build()
                );
            } catch (NoSuchManagedEntityException e) {
                log.error("This dependency references the managed entity with name '{}', but no such entity exists.", dependencyName);
                throw new InvalidDependencyException(container.getName(), dependencyName, e);
            } catch (IllegalArgumentException e) {
                log.error("Validation failed for injected field '{}' in managed entity '{}'.", injectedField.getName(), container.getName());
                throw new InvalidDependencyException(container.getName(), dependencyName, e);
            }
        }
    }

    /**
     * Discovers all dependencies that are present in the provider method of the managed entity as parameters.
     * Parameters not annotated or ones annotated with {@link Injected} will be discovered and added to the {@code container}.
     * @param container The container.
     * @throws InvalidDependencyException If there is an illegal dependency relation or invalid parameter annotated.
     */
    private void discoverProviderMethodDependencies(AbstractContainer<?> container) throws InvalidDependencyException {
        if(container.getManagedEntityConstructor().getConstructionMethod() == ConstructionMethod.PROVIDER) {
            //TODO: discover parameters of provider methods (the ones not annotated or annotated with @Injected)
            //TODO: create implicit dependency the provided managed entity depends on the entity the provider is declared in
            //TODO: because instance of it is needed to call provider.
        } else {
            log.debug("Managed entity '{}' is not constructed with provider method, skipping discovering provider method dependencies...",
                    container.getName());
        }
    }

    /**
     * Discovers all dependencies that are present in the constructor of the managed entity as parameters.
     * Parameters not annotated or ones annotated with {@link Injected} will be discovered and added to the {@code container}.
     * @param container The container.
     * @throws InvalidDependencyException If there is an illegal dependency relation or invalid parameter annotated, or the constructor
     * cannot be selected.
     */
    private void discoverConstructorDependencies(AbstractContainer<?> container) throws InvalidDependencyException {
        if(container.getManagedEntityConstructor().getConstructionMethod() == ConstructionMethod.PUBLIC_CONSTRUCTOR) {
            //TODO: discover parameters of constructor (the ones not annotated or annotated with @Injected)
        } else {
            log.debug("Managed entity '{}' is not constructed using constructor, skipping discovering constructor dependencies...",
                    container.getName());
        }
    }

    /**
     * Saves a managed entity (represented by name) to the dependency graph.
     */
    private void addManagedEntityToGraph(String entityName) {
        boolean dependencyGraphModified = dependencyGraph.addNode(entityName);
        if(dependencyGraphModified) log.debug("Node '{}' was added to the dependency graph.", entityName);
    }

    /**
     * Adds new directed edge in dependency graph between entity and its dependency. Edge will point from
     * dependency to entity.
     * @param managedEntityName Name of entity.
     * @param dependencyName Name of dependency.
     */
    private void addDependencyRelationToGraph(String managedEntityName, String dependencyName) {
        boolean dependencyGraphModified = dependencyGraph.putEdge(dependencyName, managedEntityName);
        if(dependencyGraphModified) log.debug("Edge '{}' -> '{}' was added to the dependency graph.", dependencyName, managedEntityName);
    }

    /**
     * Calculates the name that the {@link Injected} annotation has. This will be by default the type of the
     * field it was placed on, or if {@link Injected#name()} was specified, then this custom name.
     * @param field The field the annotation was placed on.
     * @param injected The annotation.
     * @return The name of the injected entity.
     */
    private String getDependencyEntityName(Field field, Injected injected) {
        return injected.name().equals(Managed.DEFAULT_MANAGED_NAME) ? field.getType().getName() : injected.name();
    }

    /**
     * Checks if the managed entity and its dependency are the same.
     */
    private boolean isSelfDependency(String managedEntityName, String dependencyEntityName) {
        return managedEntityName.equals(dependencyEntityName);
    }

    /**
     * Run checks on completed dependency graph.
     * @throws InvalidDependencyException If the graph is invalid.
     */
    private void checkCompletedDependencyGraph() throws InvalidDependencyException {
        if(Graphs.hasCycle(dependencyGraph)) {
            //TODO: return in message the actual cycle. Guava cant do this. Find better graph library
            throw new InvalidDependencyException("The dependency graph has circular dependencies!");
        }
    }

    /**
     * Utility method to resolve the dependencies of an {@link AbstractContainer} instance. It will
     * inject all dependencies to the instance.
     * @param instance The instance whose dependencies must be injected.
     * @param managedEntityName Name of the entity to which the instance belongs.
     * @param dependencies List of dependencies of this entity. Only the ones that are to be injected AFTER
     *                     construction will be processed.
     * @throws InvalidDependencyException If the resolving of the dependencies failed.
     */
    public static <T> void resolveAfterConstructDependencies(
            T instance,
            String managedEntityName,
            List<DependencyInformation<?>> dependencies
    ) throws InvalidDependencyException {
        for(DependencyInformation dependency: dependencies) {
            try {
                AbstractContainer<?> dependencyContainer = dependency.getDependencyContainer();
                Objects.requireNonNull(dependencyContainer);
                if(dependency.getInjectionType() == InjectionType.FIELD_INJECTION) {
                    Field injectedField = dependency.getInjectedField();
                    //injected field must be not null in this case!
                    Objects.requireNonNull(injectedField);
                    injectedField.setAccessible(true);
                    injectedField.set(instance, dependencyContainer.grabInstance());
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new InvalidDependencyException(managedEntityName, dependency.getDependencyContainer().getName(), e);
            }
        }
    }

}
