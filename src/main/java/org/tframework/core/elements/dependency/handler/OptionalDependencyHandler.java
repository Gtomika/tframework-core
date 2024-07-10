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
package org.tframework.core.elements.dependency.handler;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.AmbiguousElementTypeException;
import org.tframework.core.elements.ElementNotFoundException;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.utils.TypeUtils;

/**
 * This {@link SpecialElementDependencyHandler} is responsible for handling dependencies that are of type {@link Optional}.
 * <ul>
 *     <li>
 *         If there is exactly one element found by type which can fit into the {@link Optional}, then the
 *         {@link Optional} will be created with that element instance as value.
 *      </li>
 *      <li>
 *          If there is no element by type which can fit into the {@link Optional}, an empty {@link Optional}
 *          will be used.
 *      </li>
 *      <li>
 *          Any exception such as {@link AmbiguousElementTypeException} will not be handled.
 *      </li>
 * </ul>
 * If you want your element to ignore this handler, inject your element by name.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class OptionalDependencyHandler implements SpecialElementDependencyHandler {

    @Override
    public Optional<Object> handleDependency(
            ElementsContainer elementsContainer,
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph
    ) {
        if(Optional.class.isAssignableFrom(dependencyDefinition.dependencyType())) {
            var optionalItemType = TypeUtils.getTypeParameter(dependencyDefinition);
            log.debug("Optional dependency detected, with item type '{}'", optionalItemType.getName());

            try {
                var dependencyElementContext = elementsContainer.getElementContext(optionalItemType);
                //registering dependency to the graph before requesting instance
                dependencyGraph.addDependency(originalElementContext, dependencyElementContext);
                var dependencyElementInstance = dependencyElementContext.requestInstance(dependencyGraph);
                //the double Optional is intentional here, as the 'SpecialElementDependencyHandler' also uses it
                return Optional.of(Optional.of(dependencyElementInstance));
            } catch (ElementNotFoundException e) {
                //the double Optional is intentional here, as the 'SpecialElementDependencyHandler' also uses it
                return Optional.of(Optional.empty());
            }
        } else {
            //this dependency is not an optional, ignoring it
            return Optional.empty();
        }
    }
}
