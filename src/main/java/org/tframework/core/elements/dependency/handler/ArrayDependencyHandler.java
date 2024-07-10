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

import java.lang.reflect.Array;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.Priority;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

/**
 * This {@link SpecialElementDependencyHandler} is responsible for handling dependencies that are arrays.
 * It will create an array, and add all the elements of the array type to it. The {@link Priority} annotation on
 * the elements will be respected when creating the array.
 * If you want your element to ignore this handler, inject your element by name.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ArrayDependencyHandler implements SpecialElementDependencyHandler {

    @Override
    public Optional<Object> handleDependency(
            ElementsContainer elementsContainer,
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph
    ) {
        if(dependencyDefinition.dependencyType().isArray()) {
            var arrayItemType = dependencyDefinition.dependencyType().getComponentType();
            log.debug("Array dependency detected, with item type '{}'", arrayItemType.getName());

            var itemElementContexts = ElementUtils.getElementContexts(elementsContainer, arrayItemType);
            log.debug("Found {} elements of type '{}', building array...", itemElementContexts.size(), arrayItemType.getName());
            //register dependencies in the graph before requesting instances
            itemElementContexts.forEach(itemElementContext ->
                    dependencyGraph.addDependency(originalElementContext, itemElementContext));

            var array = Array.newInstance(arrayItemType, itemElementContexts.size());
            for(int i = 0; i < itemElementContexts.size(); i++) {
                var itemElementInstance = itemElementContexts.get(i).requestInstance(dependencyGraph);
                Array.set(array, i, itemElementInstance);
            }

            return Optional.of(array);
        } else {
            return Optional.empty();
        }
    }
}
