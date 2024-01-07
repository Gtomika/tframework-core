/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

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

}
