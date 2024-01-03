/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

}
