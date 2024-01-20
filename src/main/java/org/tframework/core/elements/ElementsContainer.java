/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencySource;

/**
 * Stores all elements of the application.
 */
@EqualsAndHashCode
public class ElementsContainer implements DependencySource {

    /**
     * All elements of the application, wrapped in {@link ElementContext}s.
     * The key is the name of the element.
     */
    private final Map<String, ElementContext> elementContexts;

    private ElementsContainer(Map<String, ElementContext> elementContexts) {
        this.elementContexts = new HashMap<>(elementContexts);
    }

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
     * Adds the given {@code elementContext} to this container.
     * @param elementContext The element context to add, must not be null.
     * @throws ElementNameNotUniqueException If an element with the same name is already stored in this container.
     */
    public synchronized void addElementContext(@NonNull ElementContext elementContext) throws ElementNameNotUniqueException {
        if(elementContexts.containsKey(elementContext.getName())) {
            throw new ElementNameNotUniqueException(elementContext.getName());
        } else {
            elementContexts.put(elementContext.getName(), elementContext);
        }
    }

    /**
     * @return How many elements are stored in this container.
     */
    public int elementCount() {
        return elementContexts.size();
    }

    /**
     * Requests an element dependency from this container.
     * @param dependencyName The name of the dependency to request.
     * @return The dependency value, which is an instance of the element with name {@code dependencyName}.
     * @throws ElementNotFoundException If no element with the given name is found.
     */
    @Override
    public Object requestDependency(String dependencyName) {
        return getElementContext(dependencyName).requestInstance();
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
        return new ElementsContainer(elementContexts.stream()
                .collect(
                        Collectors.toMap(
                                ElementContext::getName,
                                elementContext -> elementContext
                        )
                ));
    }
}
