/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.field;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.tframework.core.elements.annotations.Element;

@Element
public class SimpleFieldScanner implements FieldScanner {

    @Override
    public Set<Field> getAllFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .collect(Collectors.toSet());
    }
}
