/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import java.util.Optional;
import org.tframework.core.elements.dependency.DependencyDefinition;

/**
 * Resolves basic (non-element) dependency definitions into dependency values. These dependency resolvers do
 * not start additional resolutions recursively, instead they simply return the dependency value.
 */
public non-sealed interface BasicDependencyResolver extends DependencyResolver {

    /**
     * Resolves the dependency definition into a dependency value.
     * @param dependencyDefinition The {@link DependencyDefinition} that should be resolved.
     * @return The resolved dependency value, or empty if this resolver could not resolve this dependency.
     */
    Optional<Object> resolveDependency(DependencyDefinition dependencyDefinition);

}
