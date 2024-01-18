/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.tframework.core.di.context.ElementContext;

/**
 * Stores all elements of the application.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ElementsContainer {

    /**
     * All elements of the application, wrapped in {@link ElementContext}s.
     * The key is the name of the element.
     */
    private final Map<String, ElementContext> elementContexts;

    /**
     * Returns the {@link ElementContext} of the element with the given type.
     * The type will be converted to a name using {@link ElementUtils#getElementNameByType(Class)
     * @param elementType Type of the requested element, must not be null.
     * @throws ElementNotFoundException If no element with the given type is found.
     */
    public ElementContext getElementContext(@NonNull Class<?> elementType) {
        String name = ElementUtils.getElementNameByType(elementType);
        return getElementContext(name);
    }

    /**
     * Returns the {@link ElementContext} of the element with the given name.
     * @param name Name of the requested element, must not be null.
     * @throws ElementNotFoundException If no element with the given name is found.
     */
    public ElementContext getElementContext(@NonNull String name) {
        return Optional.ofNullable(elementContexts.get(name))
                .orElseThrow(() -> new ElementNotFoundException(name));
    }

    /**
     * @return How many elements are stored in this container.
     */
    public int elementCount() {
        return elementContexts.size();
    }

    /**
     * Creates an {@link ElementsContainer} that has no elements.
     */
    static ElementsContainer empty() {
        return new ElementsContainer(Map.of());
    }

    /**
     * Creates a new {@link ElementsContainer}, storing the given {@code elementContexts} as well.
     */
    static ElementsContainer fromElementContexts(@NonNull Collection<ElementContext> elementContexts) {
        //TODO implement and test
        return new ElementsContainer(elementContexts.stream()
                .collect(
                        Collectors.toMap(
                                ElementContext::getName,
                                elementContext -> elementContext
                        )
                ));
    }
}
