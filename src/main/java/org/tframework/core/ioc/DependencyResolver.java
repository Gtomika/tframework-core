package org.tframework.core.ioc;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.tframework.core.ApplicationContext;
import org.tframework.core.ioc.annotations.Injected;
import org.tframework.core.ioc.containers.AbstractContainer;
import org.tframework.core.ioc.exceptions.IocException;

import java.lang.reflect.Field;
import java.util.List;

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
     * Create a dependency resolver with an empty dependency graph.
     */
    protected DependencyResolver() {
        dependencyGraph = GraphBuilder.directed()
                .allowsSelfLoops(false)
                .build();
    }

    /**
     * Investigates the managed entities and finds the dependencies for each of them. These will be
     * saved as {@link DependencyInformation} objects in the containers of the managed entities.
     * <p>
     * Note that this does not resolve the dependencies, only discovers them. For example if managed entity 'A'
     * has an {@link org.tframework.core.ioc.annotations.Injected} field of managed entity 'B', then this dependency
     * will be discovered and saved in the container of 'A', and also in the {@link #dependencyGraph}.
     * @throws IocException If the dependencies could not be discovered or an illegal relation was found between them.
     * The cause exception contains the details.
     */
    public void discoverDependencies() throws IocException {
        var repository = ApplicationContext.getInstance().getTFrameworkIoc().getManagedEntitiesRepository();
        for(AbstractContainer<?> container: repository.iterateManagedEntities()) {
            discoverDependenciesOfEntity(container);
        }
    }

    private void discoverDependenciesOfEntity(AbstractContainer<?> container) {
        List<Field> injectedFields = FieldUtils.getFieldsListWithAnnotation(container.getInstanceType(), Injected.class);
        log.debug("Found {} fields in managed entity '{}' annotated with @Injected", injectedFields.size(), container.getName());
        if(!injectedFields.isEmpty()) {
            //this entity has at least some dependencies
            addManagedEntityToGraph(container.getName());
        }
        for(Field injectedField: injectedFields) {
            log.debug("Checking the @Injected annotated field '{}' of managed entity '{}'", injectedField.getName(), container.getName());
            Injected injectedAnnotation = injectedField.getAnnotation(Injected.class);
            //TODO: save dependency in container as DependencyInformation
            //TODO: save dependency in graph as edge, put dependent node if needed
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
     * Injects the dependencies into each managed entity. This method assumes the dependencies are discovered and valid (
     * can be done with {@link #discoverDependencies()}).
     * @throws IocException If the dependencies could not be injected.
     */
    public void resolveDependencies() throws IocException {

    }

}
