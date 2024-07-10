/*
Copyright 2023 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core.utils;

import java.lang.reflect.Executable;
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
     * Returns a nice string representation of the given method or constructor, which includes both the name,
     * and the parameter types.
     * @param executable The {@link Executable}. This argument must not be null.
     */
    public static String niceExecutableName(@NonNull Executable executable) {
        var parameterTypes = List.of(executable.getParameterTypes());
        String parameterTypesString = parameterTypes.isEmpty() ? "" : String.join(", ", classNames(parameterTypes, true));
        return executable.getName() + "(" + parameterTypesString + ")";
    }


}
