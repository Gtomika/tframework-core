/* Licensed under Apache-2.0 2024. */
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
    public Optional<Object> handleSpecialDependency(
            ElementsContainer elementsContainer,
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph
    ) {
        if(dependencyDefinition.dependencyType().isArray()) {
            var arrayItemType = dependencyDefinition.dependencyType().getComponentType();
            log.debug("Array dependency detected, with item type '{}'", arrayItemType.getName());

            var itemElements = ElementUtils.getElementInstances(elementsContainer, arrayItemType, dependencyGraph);
            log.debug("Found {} elements of type '{}', building array...", itemElements.size(), arrayItemType.getName());

            var array = Array.newInstance(arrayItemType, itemElements.size());
            for(int i = 0; i < itemElements.size(); i++) {
                Array.set(array, i, itemElements.get(i));
            }

            return Optional.of(array);
        } else {
            return Optional.empty();
        }
    }
}
