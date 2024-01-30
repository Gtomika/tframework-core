/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

/**
 * Resolves element dependency definitions into element instances. This requires more complex logic
 * than the {@link BasicDependencyResolver}s, because each dependency resolution can trigger additional resolutions,
 * and we have to avoid getting into infinite loops (such as circular dependencies).
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public abstract non-sealed class ElementDependencyResolver implements DependencyResolver {

    protected final ElementsContainer elementsContainer;

    /**
     * Resolves the given definition into an element instance.
     * @param dependencyDefinition {@link DependencyDefinition} which describes the element dependency.
     * @param originalElementContext The original {@link ElementContext} whose dependencies are being resolved.
     * @param dependencyGraph {@link ElementDependencyGraph} with the current state of the dependency resolution.
     * @return {@link Optional} with the element instance, if resolved, empty if not.
     */
    public abstract Optional<Object> resolveDependency(
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph
    );

}
