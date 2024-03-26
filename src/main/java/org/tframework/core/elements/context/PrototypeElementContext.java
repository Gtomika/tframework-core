/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;

/**
 * An {@link ElementContext} that represents a prototype element.
 * @see ElementScope#PROTOTYPE
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public final class PrototypeElementContext extends ElementContext {

    private final List<Object> instances;

    /**
     * Creates a prototype element context. For the parameter documentation, see the superclass constructor.
     */
    public PrototypeElementContext(
            String name,
            Class<?> type,
            ElementSource source,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        super(name, type, ElementScope.PROTOTYPE, source, dependencyResolutionInput);
        this.instances = new ArrayList<>();
    }

    @Override
    public void initialize() {
        log.debug("Prototype element context '{}' does not need any initialization, skipping.", name);
    }

    @Override
    protected InstanceRequest requestInstanceInternal(ElementDependencyGraph dependencyGraph) {
        Object instance = elementAssembler.assemble(dependencyGraph);

        instances.add(instance);
        log.debug("Created new instance of prototype element: {}", name);
        return InstanceRequest.ofNewlyCreated(instance);
    }

    @Override
    public String toString() {
        return "PrototypeElementContext{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", source=" + source +
                '}';
    }
}
