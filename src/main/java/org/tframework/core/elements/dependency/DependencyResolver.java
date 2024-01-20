/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency;

import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

/**
 * Resolves dependency definitions into dependency values. Since the framework is based on annotations,
 * the dependency definitions are {@link AnnotatedElement}s: fields, parameters. The dependency values
 * are the objects that will be injected into those fields or parameters.
 */
public interface DependencyResolver {

    /**
     * Resolves the dependency definition into a dependency value.
     * @param dependencySource The {@link DependencySource} from which to request the dependency.
     * @param dependencyDefinition The dependency definition that should be resolved.
     * @return The resolved dependency value, or empty if this resolver could not resolve this dependency.
     */
    Optional<Object> resolveDependency(DependencySource dependencySource, AnnotatedElement dependencyDefinition);

}
