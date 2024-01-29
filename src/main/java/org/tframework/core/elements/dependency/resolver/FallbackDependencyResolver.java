/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementUtils;
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
public class FallbackDependencyResolver extends ElementDependencyResolver {

    FallbackDependencyResolver(ElementsContainer dependencySource) {
        super(dependencySource);
    }

    @Override
    public Optional<Object> resolveDependency(
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph
    ) {
        //we have no '@InjectX' annotations, so the type will be used as dependency name
        String dependencyName = ElementUtils.getElementNameByType(dependencyDefinition.dependencyType());
        try {
            ElementContext dependencyElementContext = elementsContainer.getElementContext(dependencyName);
            // graph will be validated at another place
            dependencyGraph.addDependency(originalElementContext, dependencyElementContext);
            Object resolvedDependency = dependencyElementContext.requestInstance(dependencyGraph);
            log.debug("Resolved dependency with name '{}' from the elements: {}", dependencyName, resolvedDependency);
            return Optional.of(resolvedDependency);
        } catch (Exception e) {
            log.debug("Failed to resolve dependency with name '{}' from the elements", dependencyName, e);
            return Optional.empty();
        }
    }
}
