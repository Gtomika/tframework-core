/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.field;

import java.lang.reflect.Field;

/**
 * Filters a collection of {@link Field}s (usually produced by a {@link FieldScanner})
 * based on certain criteria, such as having an annotation.
 */
public interface FieldFilter {

    /**
     * Checks if the given fields has the static modifier.
     */
    boolean isStatic(Field field);

    /**
     * Checks if the given field has the final modifier.
     */
    boolean isFinal(Field field);
}
