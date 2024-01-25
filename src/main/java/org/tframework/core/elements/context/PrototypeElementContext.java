/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.assembler.ElementAssemblersFactory;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.dependency.DependencyResolutionInput;
import org.tframework.core.elements.dependency.graph.DependencyGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link ElementContext} that represents a prototype element.
 * @see ElementScope#PROTOTYPE
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public final class PrototypeElementContext extends ElementContext {

    private List<Object> instances;

    public PrototypeElementContext(
            String name,
            Class<?> type,
            ElementSource source
    ) {
        super(name, type, ElementScope.PROTOTYPE, source);
    }

    @Override
    public void initialize(DependencyResolutionInput input) {
        elementAssembler = ElementAssemblersFactory.createElementAssembler(name, type, source, input);
        log.trace("Initialized prototype element context: {}", name);
    }

    @Override
    public Object requestInstance(DependencyGraph dependencyGraph) {
        if(instances == null) {
            instances = new ArrayList<>();
        }
        Object instance = elementAssembler.assemble(); //TODO: add dependency graph
        instances.add(instance);
        log.trace("Created new instance of prototype element: {}", name);
        return instance;
    }
}
