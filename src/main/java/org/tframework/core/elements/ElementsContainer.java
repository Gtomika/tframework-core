/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.TFrameworkInternal;
import org.tframework.core.elements.annotations.PreConstructedElement;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencySource;

/**
 * Stores all elements of the application.
 */
@Slf4j
@EqualsAndHashCode
@PreConstructedElement
public class ElementsContainer implements DependencySource {

    /**
     * All elements of the application, wrapped in {@link ElementContext}s.
     * The key is the name of the element.
     */
    private final Map<String, ElementContext> elementContexts;
    private final boolean initialized;

    private ElementsContainer(Map<String, ElementContext> elementContexts) {
        this.elementContexts = new HashMap<>(elementContexts);
        this.initialized = false;
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
     * Checks if the element with the given name is stored in this container.
     * @param name Name of the element to check, must not be null.
     */
    public boolean hasElementContext(@NonNull String name) {
        return elementContexts.containsKey(name);
    }

    /**
     * Checks if the element with the given type is stored in this container.
     * The type will be converted to a name using {@link ElementUtils#getElementNameByType(Class)
     * @param elementType Type of the element to check, must not be null.
     */
    public boolean hasElementContext(@NonNull Class<?> elementType) {
        String name = ElementUtils.getElementNameByType(elementType);
        return hasElementContext(name);
    }

    /**
     * Adds the given {@link ElementContext} to this container. The container cannot have another element context
     * with this name. If that is the case, and it should be overridden, use {@link #overrideElementContext(ElementContext)}.
     * @param elementContext The element context to add, must not be null.
     * @throws ElementNameNotUniqueException If an element with the same name is already stored in this container.
     */
    public void addElementContext(@NonNull ElementContext elementContext) throws ElementNameNotUniqueException {
        if(elementContexts.containsKey(elementContext.getName())) {
            var existingContext = elementContexts.get(elementContext.getName());
            throw new ElementNameNotUniqueException(existingContext, elementContext);
        } else {
            elementContexts.put(elementContext.getName(), elementContext);
        }
    }

    /**
     * Adds the given {@link ElementContext} to the container. If the container already has a context with this name,
     * it will be overridden.
     * @param elementContext The element context to add, must not be null.
     * @return True if there was an override, false if there was no element context with this name.
     */
    public boolean overrideElementContext(@NonNull ElementContext elementContext) {
        boolean override = false;
        if(log.isDebugEnabled() && elementContexts.containsKey(elementContext.getName())) {
            var existingContext = elementContexts.get(elementContext.getName());
            log.debug("""
                    Overriding element context with name '{}:
                    - Existing context: {}
                    - Overriding context: {}""", existingContext.getName(), existingContext, elementContext);
            override = true;
        }
        elementContexts.put(elementContext.getName(), elementContext);
        return override;
    }

    /**
     * @return How many elements are stored in this container.
     */
    public int elementCount() {
        return elementContexts.size();
    }

    /**
     * Performs {@link ElementContext#initialize()} on all contexts in this container.
     * This method can only be called once, which is done by the framework. It should be called after all element contexts have been added.
     */
    @TFrameworkInternal
    public void initializeElementContexts() {
        if(initialized) {
            throw new IllegalStateException("This container has already been initialized");
        }
        elementContexts.values().forEach(ElementContext::initialize);
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
    public static ElementsContainer empty() {
        return new ElementsContainer(Map.of());
    }

    /**
     * Creates a new {@link ElementsContainer}, storing the given {@code elementContexts} as well.
     */
    public static ElementsContainer fromElementContexts(@NonNull Collection<ElementContext> elementContexts) {
        return new ElementsContainer(elementContexts.stream()
                .collect(
                        Collectors.toMap(
                                ElementContext::getName,
                                elementContext -> elementContext
                        )
                ));
    }
}
