/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Utilities for logging.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class LogUtils {

    /**
     * Returns the names of the classes of the given objects.
     * @param objects The objects. This collection must not be null.
     */
    public static List<String> objectClassNames(@NonNull Collection<?> objects) {
        return objects.stream()
                .map(object -> object.getClass().getName())
                .toList();
    }

    /**
     * Returns the names of the given classes.
     * @param classes The classes. This collection must not be null.
     * @param useShortName Whether to use the short name of the class (without the package name).
     */
    public static List<String> classNames(@NonNull Collection<Class<?>> classes, boolean useShortName) {
        return classes.stream()
                .map(clazz -> {
                    if (useShortName) {
                        return clazz.getSimpleName();
                    } else {
                        return clazz.getName();
                    }
                })
                .toList();
    }

    /**
     * Returns a nice string representation of the given method, which includes both the name,
     * and the parameter types.
     * @param method The method. This argument must not be null.
     */
    public static String niceMethodName(@NonNull Method method) {
        var parameterTypes = List.of(method.getParameterTypes());
        String parameterTypesString = parameterTypes.isEmpty() ? "" : String.join(", ", classNames(parameterTypes, true));
        return method.getName() + "(" + parameterTypesString + ")";
    }

}
