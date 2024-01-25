/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.assembler.ElementAssemblersFactory;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.dependency.DependencyResolutionInput;
import org.tframework.core.elements.dependency.graph.DependencyGraph;

/**
 * An {@link ElementContext} that represents a singleton element.
 * @see ElementScope#SINGLETON
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public final class SingletonElementContext extends ElementContext {

    private Object instance;

    public SingletonElementContext(String name, Class<?> type, ElementSource source) {
        super(name, type, ElementScope.SINGLETON, source);
    }

    @Override
    public void initialize(DependencyResolutionInput input) {
        elementAssembler = ElementAssemblersFactory.createElementAssembler(name, type, source, input);
        instance = requestInstance();
        log.trace("Initialized singleton element context: {}. The instance was created: {}", name, instance);
    }

    @Override
    public Object requestInstance(DependencyGraph dependencyGraph) {
        if(instance == null) {
            instance = elementAssembler.assemble(); //TODO: add dependency graph
        }
        return instance;
    }
}
