/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.context.ElementContext;

/**
 * Encapsulates the logic of getting elements by type. See
 * {@link #getElementByType(List, Class)} for the rules.
 */
@Slf4j
public class ElementByTypeResolver {

    /**
     * Selects an {@link ElementContext} from the provided ones, based on the required type. The rules are as follows:
     * <strong>Exact matching</strong><br><br>
     * At first, elements that have exactly {@code requiredType} are checked.
     * <ul>
     *     <li>If there is only one such context, it is returned.</li>
     *     <li>If there are multiple ones, an {@link AmbiguousElementTypeException} is raised.</li>
     *     <li>If there is no such context, proceeding to assignable matching.</li>
     * </ul>
     * <strong>Assignable matching</strong><br><br>
     * Since there was no element with exactly the required type, an attempt will be made to find elements
     * where the type is <b>assignable</b> to {@code requiredType}.
     * <ul>
     *     <li>If there is only one assignable element, that is returned.</li>
     *     <li>If there are multiple assignable elements, an {@link AmbiguousElementTypeException} is raised.</li>
     *     <li>If there are no assignable elements, an {@link ElementNotFoundException} is raised.</li>
     * </ul>
     * @param elementContexts The {@link ElementContext}s to select from.
     * @param requiredType The type to resolve.
     * @return An {@link ElementContext} whose type is guaranteed to be assignable to {@code requiredType}.
     * @throws ElementNotFoundException If there was no element that could be assigned to {@code requiredType}.
     * @throws AmbiguousElementTypeException If there are multiple candidate elements, and it cannot be
     *          determined which one to select.
     */
    public ElementContext getElementByType(List<ElementContext> elementContexts, Class<?> requiredType) {
        var exactTypeMatchElement = selectElementWithExactType(elementContexts, requiredType);
        if(exactTypeMatchElement.isPresent()) {
            log.trace("Found one element that has exactly '{}' type: {}", requiredType.getName(), exactTypeMatchElement.get());
            return exactTypeMatchElement.get();
        } else {
            log.trace("Found no element with exact type '{}', proceeding to assignable matching", requiredType.getName());
            var assignableTypeMatchElement = selectElementWithAssignableType(elementContexts, requiredType);
            if(assignableTypeMatchElement.isPresent()) {
                log.trace("Found exactly one element which is assignable to type '{}': {}",
                        requiredType.getName(), assignableTypeMatchElement.get());
                return assignableTypeMatchElement.get();
            } else {
                log.trace("No elements are assignable to type '{}'", requiredType.getName());
                throw new ElementNotFoundException(requiredType);
            }
        }
    }

    /**
     * Checks if there is an {@link ElementContext} in the list which is assignable to the {@code requiredType}.
     * @param elementContexts The {@link ElementContext}s to check.
     * @param requiredType The type to check.
     * @return True if there is at least one context which has assignable type to {@code requiredType}.
     */
    public boolean hasElementByType(List<ElementContext> elementContexts, Class<?> requiredType) {
        var elementsWithAssignableType = filterElementsWithAssignableType(elementContexts, requiredType);
        return !elementsWithAssignableType.isEmpty();
    }

    /**
     * Selects an element which has exactly the type.
     * @throws AmbiguousElementTypeException If there are multiple elements with the exact type.
     */
    private Optional<ElementContext> selectElementWithExactType(List<ElementContext> elementContexts, Class<?> requiredType) {
        var elementsWithExactType = filterElementsWithExactType(elementContexts, requiredType);
        return switch(elementsWithExactType.size()) {
            case 0 -> Optional.empty();
            case 1 -> Optional.of(elementsWithExactType.getFirst());
            default -> throw new AmbiguousElementTypeException(requiredType, elementsWithExactType);
        };
    }

    /**
     * Select an element which is assignable to the type.
     * @throws AmbiguousElementTypeException If multiple elements are assignable.
     * @throws ElementNotFoundException If no elements are assignable.
     */
    private Optional<ElementContext> selectElementWithAssignableType(List<ElementContext> elementContexts, Class<?> requiredType) {
        var elementsWithAssignableType = filterElementsWithAssignableType(elementContexts, requiredType);
        return switch (elementsWithAssignableType.size()) {
            case 0 -> throw new ElementNotFoundException(requiredType);
            case 1 -> Optional.of(elementsWithAssignableType.getFirst());
            default -> throw new AmbiguousElementTypeException(requiredType, elementsWithAssignableType);
        };
    }

    private List<ElementContext> filterElementsWithExactType(List<ElementContext> contexts, Class<?> requiredType) {
        return contexts.stream()
                .filter(context -> context.getType().equals(requiredType))
                .toList();
    }

    public List<ElementContext> filterElementsWithAssignableType(List<ElementContext> contexts, Class<?> requiredType) {
        return contexts.stream()
                .filter(context -> requiredType.isAssignableFrom(context.getType()))
                .toList();
    }

}
