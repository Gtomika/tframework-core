/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

/**
 * Resolves dependency definitions into dependency values. Since the framework is based on annotations,
 * the dependency definitions are {@link AnnotatedElement}s: fields, parameters. The dependency values
 * are the objects that will be injected into those fields or parameters.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DependencyResolver {

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
