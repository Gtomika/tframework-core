/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver.helper;

import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

/**
 * Contains reusable logic for resolving an element dependency by <b>type</b>.
 */
@Slf4j
public class ElementByTypeResolverHelper implements ElementDependencyResolverHelper {

    @Override
    public Object resolveElementDependency(
            ElementsContainer elementsContainer,
            ElementContext originalElementContext,
            DependencyDefinition dependencyDefinition,
            String dependencyName,
            ElementDependencyGraph dependencyGraph
    ) {
        log.debug("Attempting to resolve dependency with type '{}' from the elements", dependencyDefinition.dependencyType());
        var dependencyElementContext = elementsContainer.getElementContext(dependencyDefinition.dependencyType());
        dependencyGraph.addDependency(originalElementContext, dependencyElementContext);
        Object resolvedDependency = dependencyElementContext.requestInstance(dependencyGraph);
        log.debug("Resolved dependency from the elements: {}", resolvedDependency);
        return resolvedDependency;
    }
}
