/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

/**
 * This {@link ElementDependencyResolver} is responsible for resolving dependencies that <b>are not</b>
 * annotated with any type of '@InjectX' annotation. The default behaviour of the framework is to resolve such
 * dependencies from the elements.
 * <p>
 * Technically, this resolver will not check for the existence of any '@InjectX' annotations, it will simply
 * assume that the dependency is not annotated with any of them. This is because the dependency resolver chain
 * is ordered, and this resolver is the last one in the chain.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FallbackDependencyResolver implements ElementDependencyResolver {

    private final ElementsContainer elementsContainer;

    @Override
    public Optional<Object> resolveDependency(
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph
    ) {
        //we have no '@InjectX' annotations, so the type will be used to resolve
        log.debug("Attempting to resolve dependency with type '{}' from the elements", dependencyDefinition.dependencyType());
        try {


            ElementContext dependencyElementContext = elementsContainer.getElementContext(dependencyDefinition.dependencyType());
            // graph will be validated at another place
            dependencyGraph.addDependency(originalElementContext, dependencyElementContext);
            Object resolvedDependency = dependencyElementContext.requestInstance(dependencyGraph);
            log.debug("Resolved dependency from the elements: {}", resolvedDependency);
            return Optional.of(resolvedDependency);
        } catch (Exception e) {
            log.debug("Failed to resolve dependency from the elements", e);
            return Optional.empty();
        }
    }
}
