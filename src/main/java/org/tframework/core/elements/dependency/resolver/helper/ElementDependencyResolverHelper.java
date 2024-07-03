/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver.helper;

import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

/**
 * Encapsulates some sort of reusable logic regarding element dependency resolution.
 */
public interface ElementDependencyResolverHelper {

    /**
     * Performs the dependency resolution logic. Exceptions can be freely
     * thrown as they are expected to be handled by the caller.
     * @param elementsContainer An {@link ElementsContainer} to resolve from.
     * @param originalElementContext The {@link ElementContext} whose dependencies are being resolved.
     * @param dependencyDefinition {@link DependencyDefinition} describing this dependency.
     * @param dependencyName Name of the dependency. This <b>can be null</b> if the
     *                       dependency is not named.
     * @param dependencyGraph {@link ElementDependencyGraph} with the current state of resolution.
     * @return The resolved dependency element instance.
     */
    Object resolveElementDependency(
            ElementsContainer elementsContainer,
            ElementContext originalElementContext,
            DependencyDefinition dependencyDefinition,
            String dependencyName,
            ElementDependencyGraph dependencyGraph
    );
}
