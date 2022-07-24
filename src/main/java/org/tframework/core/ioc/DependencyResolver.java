package org.tframework.core.ioc;

import lombok.extern.slf4j.Slf4j;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.tframework.core.ApplicationContext;
import org.tframework.core.exceptions.TFrameworkRuntimeException;
import org.tframework.core.ioc.annotations.Injected;
import org.tframework.core.ioc.annotations.InjectingAnnotations;
import org.tframework.core.ioc.constants.ConstructionMethod;
import org.tframework.core.ioc.constants.InjectionType;
import org.tframework.core.ioc.containers.AbstractContainer;
import org.tframework.core.ioc.exceptions.InvalidDependencyException;
import org.tframework.core.ioc.exceptions.NoSuchManagedEntityException;
import org.tframework.core.ioc.exceptions.NotConstructibleException;
import org.tframework.core.properties.EnvironmentalVariableProperty;
import org.tframework.core.properties.PropertyContainer;
import org.tframework.core.properties.annotations.EnvironmentalVariable;
import org.tframework.core.properties.annotations.Property;
import org.tframework.core.properties.exceptions.NoSuchPropertyException;
import org.tframework.core.properties.exceptions.PropertyException;

import java.lang.reflect.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Investigates the managed entities and resolves the dependencies between them. Managed entities can depend on:
 * <ul>
 *     <li>Other managed entities.</li>
 *     <li>Properties.</li>
 * </ul>
 */
@Slf4j
public class DependencyResolver {

    /**
     * A directed graph containing the dependency relations. If entity 'A' depends on entity 'B', then
     * there will be two nodes, 'A' and 'B', and there will be a directed edge pointing from 'B' to 'A'.
     * <p>
     * Initialized with {@link #discoverDependencies()}.
     */
    private final SimpleDirectedGraph<String, DefaultEdge> dependencyGraph;

    /**
     * Create a dependency resolver with an empty dependency graph.
     */
    protected DependencyResolver() {
        dependencyGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
        //managed entity and property repositories cannot be fields -> application context
        //is not yet initialized at construction time
        log.debug("Dependency resolver has been constructed with empty dependency graph.");
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
        var managedEntitiesRepository = ApplicationContext.getInstance().getTFrameworkIoc().getManagedEntitiesRepository();
        log.info("Starting to discover the dependencies of the managed entities...");
        for(AbstractContainer<?> container: managedEntitiesRepository.iterateManagedEntities()) {
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
        log.debug("Attempting to discover the field injected dependencies of the managed entity '{}'...", container.getName());

        discoverFieldInjectionDependencies(container);
        var managedEntityConstructor = container.getManagedEntityConstructor();

        if(managedEntityConstructor.getConstructionMethod() == ConstructionMethod.PROVIDER) {
            log.debug("Attempting to discover the provider method injected dependencies of the managed entity '{}'...", container.getName());
            discoverExecutableDependencies(container, managedEntityConstructor.getProviderMethod());
        } else {
            log.debug("Managed entity '{}' is not constructed with provider method, skipping discovering provider method parameter dependencies...",
                    container.getName());
        }

        if(managedEntityConstructor.getConstructionMethod() == ConstructionMethod.PUBLIC_CONSTRUCTOR) {
            log.debug("Attempting to discover the constructor injected dependencies of the managed entity '{}'...", container.getName());
            discoverExecutableDependencies(container, managedEntityConstructor.getSelectedConstructor());
        } else {
            log.debug("Managed entity '{}' is not constructed with constructor, skipping discovering constructor parameter dependencies...",
                    container.getName());
        }
    }

    /**
     * Discovers all dependencies that are present through field injection, where a field is annotated with {@link Injected}.
     * @param container The container.
     * @throws InvalidDependencyException If there is an illegal dependency relation or invalid field annotated.
     */
    private void discoverFieldInjectionDependencies(AbstractContainer<?> container) throws InvalidDependencyException {
        Field[] fields = container.getInstanceType().getDeclaredFields();
        for(Field field: fields) {
            log.debug("Checking the field '{}' of managed entity '{}' for injection", field.getName(), container.getName());
            if(IocUtils.hasAtLeastOneInjectingAnnotation(field)) {
                try {
                    var injectingAnnotation = IocValidator.validateInjectedField(field);
                    if(injectingAnnotation == null) continue;
                    if(injectingAnnotation == Injected.class) {
                        discoverFieldInjectedManagedEntity(container, field);
                    } else if(injectingAnnotation == Property.class) {
                        discoverFieldInjectedProperty(container, field);
                    } else if(injectingAnnotation == EnvironmentalVariable.class) {
                        discoverFieldInjectedEnvironmentalVariable(container, field);
                    } else {
                        log.error("Internal error: injecting annotation '{}' is not handled when discovering field dependencies.",
                                injectingAnnotation.getName());
                        throw new TFrameworkRuntimeException("Unhandled injecting annotation:" + injectingAnnotation.getName());
                    }
                } catch (IllegalArgumentException e) {
                    log.error("Validation failed for injected field '{}' in managed entity '{}'.", field.getName(), container.getName());
                    throw new InvalidDependencyException(container.getName(), field.getName(), e);
                }
            } else {
                log.debug("The field '{}' of the managed entity '{}' is not marked for injection, ignoring...",
                        field.getName(), container.getName());
            }
        }
    }

    /**
     * Register a managed entity dependency (injected to a field) to another managed entity.
     * @param container The container or the managed entity with the field.
     * @param injectedField The field where the dependency will be injected. Assumed to be annotated with {@link Injected}.
     * @throws InvalidDependencyException If this dependency relation is not valid.
     */
    private void discoverFieldInjectedManagedEntity(AbstractContainer<?> container, Field injectedField) throws InvalidDependencyException {
        var managedEntitiesRepository = ApplicationContext.getInstance().getTFrameworkIoc().getManagedEntitiesRepository();
        String dependencyName = IocUtils.getReferencedEntityName(injectedField);
        try {
            var dependencyContainer = managedEntitiesRepository.grabManagedEntityContainer(dependencyName);
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
            log.error("Invalid dependency in managed entity '{}', field '{}'", container.getName(), injectedField.getName());
            throw new InvalidDependencyException(container.getName(), dependencyName, e);
        }
    }

    /**
     * Register a property dependency (injected to a field) to a managed entity.
     * @param container The container or the managed entity with the field.
     * @param injectedField The field where the property will be injected. Assumed to be annotated with {@link Property}.
     * @throws InvalidDependencyException If this dependency relation is not valid.
     */
    private void discoverFieldInjectedProperty(AbstractContainer<?> container, Field injectedField) throws InvalidDependencyException {
        var propertyRepository = ApplicationContext.getInstance().getPropertyRepository();
        String propertyName = injectedField.getAnnotation(Property.class).value();
        try {
            var propertyContainer = propertyRepository.grabPropertyContainer(propertyName);
            log.debug("This dependency references the property '{}'.", propertyName);
            container.addDependency(
                    DependencyInformation.builder()
                            .dependencyContainer(propertyContainer)
                            .injectionType(InjectionType.FIELD_INJECTION)
                            .injectedField(injectedField)
                            .build()
            );
        } catch (NoSuchPropertyException e) {
            log.error("This dependency references the property '{}', but no such property exists.", propertyName);
            throw new InvalidDependencyException(container.getName(), injectedField.getName(), e);
        }
    }

    /**
     * Register an environmental variable dependency (injected to a field) to a managed entity.
     * @param container The container or the managed entity with the field.
     * @param injectedField The field where the variable will be injected. Assumed to be annotated with {@link EnvironmentalVariable}.
     * @throws InvalidDependencyException If this dependency relation is not valid.
     */
    private void discoverFieldInjectedEnvironmentalVariable(AbstractContainer<?> container, Field injectedField) throws InvalidDependencyException {
        String environmentalVariableName = injectedField.getAnnotation(EnvironmentalVariable.class).value();
        log.debug("This dependency references the environmental variable '{}'.", environmentalVariableName);
        EnvironmentalVariableProperty environmentalVariableProperty = new EnvironmentalVariableProperty(environmentalVariableName);
        var propertyContainer = new PropertyContainer<>(environmentalVariableProperty);
        container.addDependency(
                DependencyInformation.<String>builder()
                        .dependencyContainer(propertyContainer)
                        .injectionType(InjectionType.FIELD_INJECTION)
                        .injectedField(injectedField)
                        .build()
        );
    }

    /**
     * Discovers all dependencies that are present in the provider method or selected constructor of the managed entity as parameters.
     * Parameters not annotated or annotated with exactly one "injecting" annotation ({@link InjectingAnnotations#getInjectingAnnotations()}).
     * @param container The container.
     * @param executable The provider method or the constructor.
     * @throws InvalidDependencyException If there is an illegal dependency relation or invalid parameter annotated.
     */
    private void discoverExecutableDependencies(AbstractContainer<?> container, Executable executable) throws InvalidDependencyException {
        for(Parameter parameter: executable.getParameters()) {
            try {
                var annotation = IocValidator.validateProviderOrConstructorParameter(parameter);
                if(annotation == null || annotation == Injected.class) {
                    discoverParameterInjectedManagedEntity(container, parameter, executable);
                } else if(annotation == Property.class) {
                    discoverParameterInjectedProperty(container, parameter, executable);
                } else if(annotation == EnvironmentalVariable.class) {
                    discoverParameterInjectedEnvironmentalVariable(container, parameter, executable);
                } else {
                    log.error("Internal error: injecting annotation '{}' is not handled when discovering parameter dependencies.",
                            annotation.getName());
                    throw new TFrameworkRuntimeException("Unhandled injecting annotation:" + annotation.getName());
                }
            } catch (IllegalArgumentException | NoSuchManagedEntityException e) {
                throw new InvalidDependencyException(container.getName(), parameter.getName(), e);
            }
        }
        //create implicit dependency: the provided managed entity depends on the entity the provider is declared in
        //because instance of it is needed to call provider.
        if(executable instanceof Method) {
            String declaringManagedEntityName = IocUtils.getReferencedEntityName(executable.getDeclaringClass());
            addDependencyRelationToGraph(container.getName(), declaringManagedEntityName);
            log.debug("Registering implicit dependency: '{}' depends on '{}' because the provider method of it " +
                    "is declared in the managed entity '{}'.", container.getName(), declaringManagedEntityName, declaringManagedEntityName);
        }
    }

    /**
     * Register a managed entity dependency (injected to a parameter) to another managed entity.
     * @param container The container or the managed entity with the field.
     * @param parameter The parameter where the dependency will be injected. Assumed to be annotated with {@link Injected}.
     * @param executable The executable to which the parameter belongs.
     * @throws InvalidDependencyException If this dependency relation is not valid.
     */
    private void discoverParameterInjectedManagedEntity(
            AbstractContainer<?> container,
            Parameter parameter,
            Executable executable
    ) throws InvalidDependencyException {
        var managedEntitiesRepository = ApplicationContext.getInstance().getTFrameworkIoc().getManagedEntitiesRepository();
        String dependencyName = IocUtils.getReferencedEntityName(parameter);
        var dependencyContainer = managedEntitiesRepository.grabManagedEntityContainer(dependencyName);
        log.debug("Executable '{}': the parameter '{}' is referencing '{}' as dependency.",
                executable.getName(), parameter.getName(), dependencyName);
        //save in the graph
        addDependencyRelationToGraph(container.getName(), dependencyName);
        //save in dependencies of the container
        container.addDependency(
                DependencyInformation.builder()
                        .dependencyContainer(dependencyContainer)
                        .injectionType(executable instanceof Constructor ?
                                InjectionType.CONSTRUCTOR_INJECTION : InjectionType.PROVIDER_INJECTION)
                        .injectedParameter(parameter)
                        .build()
        );
    }

    /**
     * Register a property dependency (injected to a parameter) to another managed entity.
     * @param container The container or the managed entity with the field.
     * @param parameter The parameter where the dependency will be injected. Assumed to be annotated with {@link Property}.
     * @param executable The executable to which the parameter belongs.
     * @throws InvalidDependencyException If this dependency relation is not valid.
     */
    private void discoverParameterInjectedProperty(
            AbstractContainer<?> container,
            Parameter parameter,
            Executable executable
    ) throws InvalidDependencyException {
        String propertyName = parameter.getAnnotation(Property.class).value();
        try {
            var propertyRepository = ApplicationContext.getInstance().getPropertyRepository();
            var propertyContainer = propertyRepository.grabPropertyContainer(propertyName);
            log.debug("This dependency references the property '{}'.", propertyName);
            container.addDependency(
                    DependencyInformation.builder()
                            .dependencyContainer(propertyContainer)
                            .injectionType(executable instanceof Constructor ?
                                    InjectionType.CONSTRUCTOR_INJECTION : InjectionType.PROVIDER_INJECTION)
                            .injectedParameter(parameter)
                            .build()
            );
        } catch (NoSuchPropertyException e) {
            log.error("This dependency references the property '{}', but no such property exists.", propertyName);
            throw new InvalidDependencyException(container.getName(), parameter.getName(), e);
        }
    }

    /**
     * Register an environmental variable dependency (injected to a parameter) to another managed entity.
     * @param container The container or the managed entity with the field.
     * @param parameter The parameter where the dependency will be injected. Assumed to be annotated with {@link EnvironmentalVariable}.
     * @param executable The executable to which the parameter belongs.
     * @throws InvalidDependencyException If this dependency relation is not valid.
     */
    private void discoverParameterInjectedEnvironmentalVariable(
            AbstractContainer<?> container,
            Parameter parameter,
            Executable executable
    ) throws InvalidDependencyException {
        String environmentalVariableName = parameter.getAnnotation(EnvironmentalVariable.class).value();
        log.debug("This dependency references the environmental variable '{}'.", environmentalVariableName);
        EnvironmentalVariableProperty environmentalVariableProperty = new EnvironmentalVariableProperty(environmentalVariableName);
        var propertyContainer = new PropertyContainer<>(environmentalVariableProperty);
        container.addDependency(
                DependencyInformation.<String>builder()
                        .dependencyContainer(propertyContainer)
                        .injectionType(executable instanceof Constructor ?
                                InjectionType.CONSTRUCTOR_INJECTION : InjectionType.PROVIDER_INJECTION)
                        .injectedParameter(parameter)
                        .build()
        );
    }

    /**
     * Saves a managed entity (represented by name) to the dependency graph.
     */
    private void addManagedEntityToGraph(String entityName) {
        boolean dependencyGraphModified = dependencyGraph.addVertex(entityName);
        if(dependencyGraphModified) log.debug("Node '{}' was added to the dependency graph.", entityName);
    }

    /**
     * Adds new directed edge in dependency graph between entity and its dependency. Edge will point from
     * dependency to entity.
     * @param managedEntityName Name of entity.
     * @param dependencyName Name of dependency.
     */
    private void addDependencyRelationToGraph(String managedEntityName, String dependencyName) {
        //vertexes will be only added if not already present
        addManagedEntityToGraph(managedEntityName);
        addManagedEntityToGraph(dependencyName);
        var newEdge = dependencyGraph.addEdge(dependencyName, managedEntityName);
        if(newEdge != null) log.debug("Edge '{}' -> '{}' was added to the dependency graph.", dependencyName, managedEntityName);
    }

    /**
     * Checks if the managed entity and its dependency are the same.
     */
    private boolean isSelfDependency(String managedEntityName, String dependencyEntityName) {
        return managedEntityName.equals(dependencyEntityName);
    }

    /**
     * Run checks on completed dependency graph.
     * @throws InvalidDependencyException If the graph is invalid. Details in exception message.
     */
    private void checkCompletedDependencyGraph() throws InvalidDependencyException {
        var cycleDetector = new CycleDetector<>(dependencyGraph);
        Set<String> cycleNodes = cycleDetector.findCycles();
        if(!cycleNodes.isEmpty()) {
            throw new InvalidDependencyException(String.format("The dependency graph has circular dependencies! The following managed " +
                    "entities form one or more cycles: %s. Refactor circular dependencies to fix this.", cycleNodes));
        }
    }

    /**
     * Finds the amount of managed entities in the dependency graph.
     */
    public int getDependencyGraphSize() {
        return dependencyGraph.vertexSet().size();
    }

    /**
     * Utility method to resolve the dependencies of a managed entity instance. It will
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
        for(DependencyInformation<?> dependency: dependencies) {
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
            } catch (IllegalArgumentException | IllegalAccessException | NotConstructibleException | PropertyException e) {
                log.error("Managed entity '{}': An exception prevented injecting the dependency '{}'.", managedEntityName,
                        dependency.getDependencyContainer().getName(), e);
                throw new InvalidDependencyException(managedEntityName, dependency.getDependencyContainer().getName(), e);
            }
        }
    }

}
