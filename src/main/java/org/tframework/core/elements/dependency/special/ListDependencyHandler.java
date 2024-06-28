/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.special;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.Priority;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

/**
 * This {@link SpecialElementDependencyHandler} is responsible for handling dependencies that are of type {@link List}.
 * It will create a list, and add all the elements of the list type to it. The {@link Priority} annotation on
 * the elements will be respected when filling the list. The created list will be mutable.
 */
@Slf4j
public class ListDependencyHandler implements SpecialElementDependencyHandler {

    @Override
    public Optional<Object> handleSpecialDependency(
            ElementsContainer elementsContainer,
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph
    ) {
        if(List.class.isAssignableFrom(dependencyDefinition.dependencyType())) {
            var listItemType = ElementUtils.getDependencyTypeParameter(dependencyDefinition);
            log.debug("List dependency detected, with item type '{}'", listItemType.getName());

            var itemElements = ElementUtils.getElementInstances(elementsContainer, listItemType, false);
            log.debug("Found {} elements of type '{}'", itemElements.size(), listItemType.getName());

            return Optional.of(new ArrayList<>(itemElements));
        } else {
            return Optional.empty();
        }
    }
}
