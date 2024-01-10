/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * A reasonable default implementation for {@link MethodScanner} that uses
 * reflections to find methods on a single class. Only methods declared on the
 * class itself will be returned (for example, methods on the superclass will not be found).
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultMethodScanner implements MethodScanner {

    private final Class<?> classToScan;

    @Override
    public Set<Method> scanMethods() {
        return Arrays.stream(classToScan.getDeclaredMethods())
                .collect(Collectors.toSet());
    }
}
