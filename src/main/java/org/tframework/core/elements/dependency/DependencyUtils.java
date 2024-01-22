package org.tframework.core.elements.dependency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Contains common operations regarding dependencies.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DependencyUtils {

    /**
     * Resolves a dependency using the given dependency resolvers.
     * @param dependencyDefinition The {@link DependencyDefinition} of the dependency to resolve.
     * @param dependencyDeclaredAs An informative string which defines where the dependency is declared.
     *                             For example 'constructor parameter' or 'field'.
     * @param dependencyResolvers The dependency resolvers to use to resolve the dependency.
     * @return The resolved dependency value.
     * @throws DependencyResolutionException If the dependency could not be resolved.
     */
    public static Object resolveDependency(
            DependencyDefinition dependencyDefinition,
            String dependencyDeclaredAs,
            List<? extends DependencyResolver> dependencyResolvers
    ) throws DependencyResolutionException {
        for(DependencyResolver resolver: dependencyResolvers) {
            var resolvedDependency = resolver.resolveDependency(dependencyDefinition);
            if(resolvedDependency.isPresent()) {
                return resolvedDependency.get();
            }
        }
        throw DependencyResolutionException.builder()
                .dependencyType(dependencyDefinition.dependencyType())
                .declaredAs(dependencyDeclaredAs)
                .usedResolvers(dependencyResolvers)
                .build();
    }

}
