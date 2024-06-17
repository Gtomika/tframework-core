/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.field;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.tframework.core.elements.annotations.Element;

@Element
public class SimpleFieldFilter implements FieldFilter {

    @Override
    public boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    @Override
    public boolean isFinal(Field field) {
        return Modifier.isFinal(field.getModifiers());
    }
}
