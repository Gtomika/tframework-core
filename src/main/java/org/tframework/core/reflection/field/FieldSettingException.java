/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.field;

import java.lang.reflect.Field;
import org.tframework.core.TFrameworkException;

/**
 * Thrown when {@link FieldSetter}s encounter exceptions.
 */
public class FieldSettingException extends TFrameworkException {

    private static final String TEMPLATE = "Failed to set field '%s' in class '%s'";

    public FieldSettingException(Field field, Class<?> clazz, Exception cause) {
        super(TEMPLATE.formatted(field.getName(), clazz.getName()), cause);
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
