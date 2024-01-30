/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

/**
 * The dependency resolver aggregator combines multiple {@link DependencyResolver}s to
 * match a dependency definition to the actual value that will be injected. Use
 * {@link #usingResolvers(List)} to make an aggregator.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DependencyResolverAggregator {

    private final List<DependencyResolver> dependencyResolvers;

    /**
     * Resolves a dependency using all resolvers of this aggregator.
     * @param dependencyDefinition The {@link DependencyDefinition} of the dependency to resolve.
     * @param originalElementContext The {@link ElementContext} whose dependencies are being resolved.
     * @param dependencyGraph {@link ElementDependencyGraph} with the current state of the resolution process.
     * @param dependencyDeclaredAs An informative string which defines where the dependency is declared.
     *                             For example 'constructor parameter' or 'field'.
     * @return The resolved dependency value.
     * @throws DependencyResolutionException If the dependency could not be resolved.
     */
    public Object resolveDependency(
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph,
            String dependencyDeclaredAs
    ) throws DependencyResolutionException {
        for(DependencyResolver resolver: dependencyResolvers) {
            var resolvedDependency = attemptResolutionWithOneResolver(
                    resolver, dependencyDefinition, originalElementContext, dependencyGraph
            );
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

    private Optional<Object> attemptResolutionWithOneResolver(
            DependencyResolver dependencyResolver,
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph
    ) {
        return switch (dependencyResolver) {
            case BasicDependencyResolver bdr -> bdr.resolveDependency(dependencyDefinition);
            case ElementDependencyResolver edr -> edr.resolveDependency(dependencyDefinition, originalElementContext, dependencyGraph);
        };
    }

    /**
     * Creates a {@link DependencyResolverAggregator} from the provided list of resolvers.
     * @param resolvers List of {@link DependencyResolver}s to use, must not be null.
     */
    public static DependencyResolverAggregator usingResolvers(@NonNull List<DependencyResolver> resolvers) {
        return new DependencyResolverAggregator(resolvers);
    }

}
