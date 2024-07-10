/*
Copyright 2024 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
