/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context;

import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;

/**
 * An {@link ElementContext} that represents a singleton element.
 * @see ElementScope#SINGLETON
 */
@Slf4j
public final class SingletonElementContext extends ElementContext {

    private Object instance;

    /**
     * Creates a singleton element context. For parameter details, see superclass constructor.
     */
    public SingletonElementContext(
            String name,
            Class<?> type,
            ElementSource source,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        super(name, type, ElementScope.SINGLETON, source, dependencyResolutionInput);
    }

    @Override
    public void initialize() {
        initialize(ElementDependencyGraph.empty());
    }

    private void initialize(ElementDependencyGraph dependencyGraph) {
        log.debug("Starting initialization of singleton element '{}'", name);
        if(instance == null) {
            instance = requestInstance(dependencyGraph);
            log.debug("Initialized singleton element context: {}. The instance was created: {}", name, instance);
        } else {
            log.debug("Singleton element '{}' was already eagerly initialized, skipping.", name);
        }
    }

    @Override
    protected InstanceRequest requestInstanceInternal(ElementDependencyGraph dependencyGraph) {
        if(instance == null) {
            instance = elementAssembler.assemble(dependencyGraph);
            return InstanceRequest.ofNewlyCreated(instance);
        } else {
            return InstanceRequest.ofReused(instance);
        }
    }

    @Override
    public String toString() {
        return "SingletonElementContext{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", source=" + source +
                '}';
    }
}
