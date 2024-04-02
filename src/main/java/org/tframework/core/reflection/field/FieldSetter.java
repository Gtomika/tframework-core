/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.field;

import java.lang.reflect.Field;

/**
 * Sets values into object's {@link Field}s.
 */
public interface FieldSetter {

    /**
     * Sets the field's value.
     * @param object The object whose field should be set.
     * @param field The {@link Field} to set. This must be a field of {@code object}.
     * @param value The value to set into {@code field}. This must be assignable to {@code field}'s type.
     * @throws FieldSettingException If an underlying exception occurred while setting the method.
     */
    void setFieldValue(Object object, Field field, Object value) throws FieldSettingException;

}
