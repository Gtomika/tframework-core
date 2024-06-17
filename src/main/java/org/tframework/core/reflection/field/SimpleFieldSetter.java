/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.field;

import java.lang.reflect.Field;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.Element;

@Slf4j
@Element
public class SimpleFieldSetter implements FieldSetter {

    @Override
    public void setFieldValue(Object object, Field field, Object value) {
        try {
            if(field.canAccess(object)) {
                field.set(object, value);
            } else {
                field.setAccessible(true);
                field.set(object, value);
                field.setAccessible(false);
            }
        } catch (Exception e) {
            throw new FieldSettingException(field, object.getClass(), e);
        }
    }
}
