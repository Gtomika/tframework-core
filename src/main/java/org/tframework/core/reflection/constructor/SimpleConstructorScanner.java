/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.constructor;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A simple implementation of {@link ConstructorScanner} that uses basic reflection.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SimpleConstructorScanner implements ConstructorScanner {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<Constructor<T>> getAllConstructors(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .map(constructor -> (Constructor<T>) constructor)
                .collect(Collectors.toSet());
    }
}
