/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.annotations.Priority;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;

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
     * @param initializeElements If true, the elements will be initialized before being returned. This is only
     *                           required if this method is called eagerly before the elements are initialized.
     * @return A list of element instances that are assignable to the given type. The ordering of this
     * list will adhere to the rules of {@link Priority} annotation.
     * @param <T> The type of elements to get.
     */
    public static <T> List<T> getElementInstances(
            ElementsContainer container,
            Class<T> elementType,
            boolean initializeElements
    ) {
        var elementContexts = container.getElementContextsWithType(elementType);
        if(initializeElements) {
            container.initializeElementContexts(elementContexts);
        }
        return elementContexts.stream()
                .sorted(PriorityAnnotationComparator.create())
                .map(ElementContext::requestInstance)
                .map(elementType::cast) //safe cast, because these are assignable
                .toList();
    }

    /**
     * Gets the type parameter of a genetic dependency definition.
     * @param dependencyDefinition The {@link DependencyDefinition} to get the type parameter from. This is assumed to
     *                             be a generic type with exactly one type parameter. For example
     *                             a {@link List} or a {@link Optional}.
     * @return The type parameter of the dependency definition. For example if the dependency definition is {@code List<String>},
     *        this method will return {@code String.class}.
     */
    public static Class<?> getDependencyTypeParameter(DependencyDefinition dependencyDefinition) {
        ParameterizedType parameterizedType = switch (dependencyDefinition.annotationSource()) {
            case Field field -> (ParameterizedType) field.getGenericType();
            case Parameter parameter -> (ParameterizedType) parameter.getParameterizedType();
            default -> throw new IllegalArgumentException("Unsupported annotation source: " + dependencyDefinition.annotationSource());
        };
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

}
