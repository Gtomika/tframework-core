/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.TFrameworkInternal;
import org.tframework.core.elements.annotations.PreConstructedElement;
import org.tframework.core.elements.context.ElementContext;

/**
 * Stores all elements of the application, and provides ways to access them by name or by type.
 */
@Slf4j
@EqualsAndHashCode
@PreConstructedElement
public class ElementsContainer implements Iterable<ElementContext> {

    /**
     * All elements of the application, wrapped in {@link ElementContext}s.
     */
    private final List<ElementContext> elementContexts;
    private final boolean initialized;
    private final ElementByTypeResolver elementByTypeResolver;

    private ElementsContainer(List<ElementContext> elementContexts) {
        this.elementContexts = new LinkedList<>(elementContexts);
        this.initialized = false;
        this.elementByTypeResolver = new ElementByTypeResolver();
    }

    ElementsContainer(List<ElementContext> elementContexts, ElementByTypeResolver resolver) {
        this.elementContexts = new LinkedList<>(elementContexts);
        this.initialized = false;
        this.elementByTypeResolver = resolver;
    }

    /**
     * Returns the {@link ElementContext} of the element with the given name.
     *
     * @param name Name of the requested element, must not be null.
     * @throws ElementNotFoundException If no element with the given name is found.
     */
    public ElementContext getElementContext(@NonNull String name) {
        return elementContexts.stream()
                .filter(context -> context.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new ElementNotFoundException(name));
    }

    /**
     * Returns the element with the given name. See {@link #getElementContext(String)} for details.
     * This method extracts the instance from the context and performs a cast.
     * @param name The element name, must not be null.
     * @param elementType The type of the element, must not be null.
     * @throws ElementNotFoundException If no element is found with the given name.
     * @throws ClassCastException If the element is not of the given type.
     * @return The element instance.
     * @param <T> Type of the element.
     */
    @SuppressWarnings("unchecked")
    public <T> T getElement(@NonNull String name, @NonNull Class<T> elementType) {
        return (T) getElementContext(name).requestInstance();
    }

    /**
     * Returns the {@link ElementContext} of the element with the given type.
     * See {@link ElementByTypeResolver#getElementByType(List, Class)} for the rules on how this type is resolved.
     *
     * @param elementType Type of the requested element, must not be null.
     * @throws ElementNotFoundException      If no element is found which is assignable to the required type.
     * @throws AmbiguousElementTypeException If there are multiple candidate elements that can be assigned to
     *                                       the type, and it cannot be determined which one to choose.
     */
    public ElementContext getElementContext(@NonNull Class<?> elementType) {
        return elementByTypeResolver.getElementByType(elementContexts, elementType);
    }

    /**
     * Returns the element with the given type. Please see {@link #getElementContext(Class)} for details.
     * This method extracts the instance from the context and performs a cast.
     * @param elementType Type of the requested element, must not be null.
     * @throws ElementNotFoundException      If no element is found which is assignable to the required type.
     * @throws AmbiguousElementTypeException If there are multiple candidate elements that can be assigned to
     *                                       the type, and it cannot be determined which one to choose.
     * @return The element instance.
     * @param <T> Type of the element.
     */
    @SuppressWarnings("unchecked")
    public <T> T getElement(@NonNull Class<T> elementType) {
        return (T) getElementContext(elementType).requestInstance();
    }

    /**
     * Checks if the element with the given name is stored in this container.
     *
     * @param name Name of the element to check, must not be null.
     */
    public boolean hasElementContext(@NonNull String name) {
        return elementContexts.stream()
                .anyMatch(context -> context.getName().equals(name));
    }

    /**
     * Checks if the element with the given type is stored in this container.
     * See {@link ElementByTypeResolver#hasElementByType(List, Class)} for the rules on how this type is resolved.
     *
     * @param elementType Type of the element to check, must not be null.
     * @return True only if there is at least element that is assignable to the type.
     */
    public boolean hasElementContext(@NonNull Class<?> elementType) {
        return elementByTypeResolver.hasElementByType(elementContexts, elementType);
    }

    /**
     * Returns all elements stored in this container that are assignable to the given type.
     * @param elementType Type of the elements to return, must not be null.
     */
    public List<ElementContext> getElementContextsWithType(@NonNull Class<?> elementType) {
        return elementByTypeResolver.filterElementsWithAssignableType(elementContexts, elementType);
    }

    /**
     * Adds the given {@link ElementContext} to this container. The container cannot have another element context
     * with this name. If that is the case, and it should be overridden, use {@link #overrideElementContext(ElementContext)}.
     * @param elementContext The element context to add, must not be null.
     * @throws ElementNameNotUniqueException If an element with the same name is already stored in this container.
     * @throws IllegalStateException If the container is already initialized.
     */
    @TFrameworkInternal
    public void addElementContext(@NonNull ElementContext elementContext) throws ElementNameNotUniqueException {
        if (initialized) {
            throw new IllegalStateException("New element context cannot be added after the container is initialized.");
        }
        try {
            var existingContext = getElementContext(elementContext.getName());
            throw new ElementNameNotUniqueException(existingContext, elementContext);
        } catch (ElementNotFoundException e) {
            elementContexts.add(elementContext);
        }
    }

    /**
     * Adds the given {@link ElementContext} to the container. If the container already has a context with this name,
     * it will be overridden.
     *
     * @param elementContext The element context to add, must not be null.
     * @return True if there was an override, false if there was no element context with this name.
     * @throws IllegalStateException If the container is already initialized.
     */
    @TFrameworkInternal
    public boolean overrideElementContext(@NonNull ElementContext elementContext) {
        if (initialized) {
            throw new IllegalStateException("Element contexts cannot be overridden after the container is initialized.");
        }
        try {
            var existingContext = getElementContext(elementContext.getName());
            log.debug("""
                    Overriding element context with name '{}:
                    - Existing context: {}
                    - Overriding context: {}""", existingContext.getName(), existingContext, elementContext);
            elementContexts.remove(existingContext);
            elementContexts.add(elementContext);
            return true;
        } catch (ElementNotFoundException e) {
            elementContexts.add(elementContext);
            return false;
        }
    }

    /**
     * Removes the given {@link ElementContext} from the container.
     *
     * @param elementContext The element context to remove, must not be null.
     * @throws IllegalStateException If the container is already initialized.
     */
    @TFrameworkInternal
    public void removeElementContext(@NonNull ElementContext elementContext) {
        if (initialized) {
            throw new IllegalStateException("Element contexts cannot be removed after the container is initialized.");
        }
        elementContexts.remove(elementContext);
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
        if (initialized) {
            throw new IllegalStateException("This container has already been initialized");
        }
        elementContexts.forEach(ElementContext::initialize);
    }

    /**
     * Similar to {@link #initializeElementContexts()}, but allows the caller to provide the
     * element contexts to initialize instead of applying to all contexts.
     * @param elementContexts The element contexts to initialize, must not be null.
     */
    public void initializeElementContexts(@NonNull List<ElementContext> elementContexts) {
        elementContexts.forEach(ElementContext::initialize);
    }

    /**
     * Creates an {@link ElementsContainer} that has no elements.
     */
    public static ElementsContainer empty() {
        return new ElementsContainer(List.of());
    }

    /**
     * Creates a new {@link ElementsContainer}, storing the given {@code elementContexts} as well.
     */
    public static ElementsContainer fromElementContexts(@NonNull Collection<ElementContext> elementContexts) {
        return new ElementsContainer(elementContexts.stream().toList());
    }

    @Override
    public Iterator<ElementContext> iterator() {
        return elementContexts.iterator();
    }
}
