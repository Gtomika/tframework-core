/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.handler;

import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

/**
 * Contains and uses a list of {@link SpecialElementDependencyHandler}s on a
 * given dependency.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SpecialDependencyHandlerAggregator {

    private final List<SpecialElementDependencyHandler> handlers;

    /**
     * Gets the object that is the result of the special dependency handling, or an empty
     * {@link Optional} if this dependency was not handled by any handler.
     * @param elementsContainer {@link ElementsContainer} to use for resolving the dependency.
     * @param dependencyDefinition {@link DependencyDefinition} that describes the dependency.
     * @param originalElementContext The original {@link ElementContext} whose dependencies are being resolved.
     * @param dependencyGraph {@link ElementDependencyGraph} with the current state of the dependency resolution.
     * @return {@link Optional} with the object that is the result of the special dependency handling. If this
     * handler cannot handle the dependency, it should return an empty {@link Optional}.
     */
    Optional<Object> handleDependency(
            ElementsContainer elementsContainer,
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph
    ) {
        for(var handler: handlers) {
            var handledResult = handler.handleDependency(
                    elementsContainer,
                    dependencyDefinition,
                    originalElementContext,
                    dependencyGraph
            );
            if(handledResult.isPresent()) {
                log.debug("Handler '{}' successfully handled the dependency {}",
                        handler.getClass().getName(), dependencyDefinition);
                return handledResult;
            }
        }
        return Optional.empty();
    }

    /**
     * Creates a {@link SpecialDependencyHandlerAggregator} using a list of handlers.
     */
    public static SpecialDependencyHandlerAggregator usingHandlers(List<SpecialElementDependencyHandler> handlers) {
        return new SpecialDependencyHandlerAggregator(handlers);
    }
}
