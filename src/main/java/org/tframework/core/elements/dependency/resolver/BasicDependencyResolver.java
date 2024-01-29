/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.DependencySource;

import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

/**
 * Resolves basic (non-element) dependency definitions into dependency values. These dependency resolvers do
 * not start additional resolutions recursively, instead they simply return the dependency value.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public non-sealed abstract class BasicDependencyResolver implements DependencyResolver {

    /**
     * The {@link DependencySource} from which to request the dependency.
     */
    protected final DependencySource dependencySource;

    /**
     * Resolves the dependency definition into a dependency value.
     * @param dependencyDefinition The {@link DependencyDefinition} that should be resolved.
     * @return The resolved dependency value, or empty if this resolver could not resolve this dependency.
     */
    public abstract Optional<Object> resolveDependency(DependencyDefinition dependencyDefinition);

}
