/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.field;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Discovers fields of a {@link Class} object.
 */
public interface FieldScanner {

    /**
     * Returns a set of all fields that are present for the given class, regardless
     * of visibility or staticness.
     */
    Set<Field> getAllFields(Class<?> clazz);

}
