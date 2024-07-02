/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.handler;

import java.util.List;
import java.util.Optional;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

/**
 * A special dependency handler is responsible for handling dependencies that should be treated
 * in a special way. For example, elements of collections, arrays.
 * If no special dependency handler can handle the dependency, the default dependency resolution will be used.
 * <p><br>
 * If you want to "get around" special dependency handling, use <b>named</b> dependencies. For example, if you want
 * to inject an actual {@link List} element without triggering {@link ListDependencyHandler}, you can inject
 * by name.
 */
public interface SpecialElementDependencyHandler {

    /**
     * Gets the object that is the result of the special dependency handling.
     * @param elementsContainer {@link ElementsContainer} to use for resolving the dependency.
     * @param dependencyDefinition {@link DependencyDefinition} that describes the dependency.
     * @param originalElementContext The original {@link ElementContext} whose dependencies are being resolved.
     * @param dependencyGraph {@link ElementDependencyGraph} with the current state of the dependency resolution.
     * @return {@link Optional} with the object that is the result of the special dependency handling. If this
     * handler cannot handle the dependency, it should return an empty {@link Optional}.
     */
    Optional<Object> handleSpecialDependency(
            ElementsContainer elementsContainer,
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph
    );

}
