/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;

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

}
