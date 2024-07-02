/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.annotations.Priority;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

/**
 * Utility class for operations on elements.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementUtils {

    /**
     * Creates a name for an element based on its type.
     */
    public static String getElementNameByType(Class<?> clazz) {
        return clazz.getName();
    }

    /**
     * Creates a string representation of an {@link Element} annotation.
     */
    public static String stringifyElementAnnotation(@NonNull Element elementAnnotation) {
        return String.format(
                "@Element(name = %s, scope = %s)",
                elementAnnotation.name().equals(Element.NAME_NOT_SPECIFIED) ? "<default>" : elementAnnotation.name(),
                elementAnnotation.scope()
        );
    }

    /**
     * Checks if a name was provided for the {@link InjectElement}. That is, anything other
     * than {@link Element#NAME_NOT_SPECIFIED} is set in {@link InjectElement#value()}.
     */
    public static boolean isNamedElementInjection(@NonNull InjectElement elementAnnotation) {
        return !elementAnnotation.value().equals(Element.NAME_NOT_SPECIFIED);
    }

    /**
     * A utility method combining logic from {@link ElementsContainer} and casting, to initialize
     * and get a list of elements that are assignable to a given type.
     * @param container The {@link ElementsContainer} to use.
     * @param elementType The type of elements to get.
     * @return A list of element instances that are assignable to the given type. The ordering of this
     * list will adhere to the rules of {@link Priority} annotation.
     * @param <T> The type of elements to get.
     */
    public static <T> List<T> getElementInstances(ElementsContainer container, Class<T> elementType) {
        return getElementInstances(container, elementType, ElementDependencyGraph.empty());
    }

    /**
     * A version of {@link #getElementInstances(ElementsContainer, Class)} which supports continuing the
     * dependency resolution from a given {@link ElementDependencyGraph}.
     */
    public static <T> List<T> getElementInstances(
            ElementsContainer container,
            Class<T> elementType,
            ElementDependencyGraph graph
    ) {
        var elementContexts = container.getElementContextsWithType(elementType);
        //if some elements are already initialized, that's not a problem, those will be skipped
        container.initializeElementContexts(elementContexts);
        return elementContexts.stream()
                .sorted(PriorityAnnotationComparator.create())
                .map(context -> context.requestInstance(graph))
                .map(elementType::cast) //safe cast, because these are assignable
                .toList();
    }
}
