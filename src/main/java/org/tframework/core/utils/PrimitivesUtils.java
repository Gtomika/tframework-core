/* Licensed under Apache-2.0 2024. */
package org.tframework.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PrimitivesUtils {

    /**
     * Makes sure a class does not belong to a primitive type. If so, it
     * will be converted to the wrapper class instead.
     * @param clazz Class to convert to wrapper, can't be null.
     * @return Class that is guaranteed to be not a primitive type.
     */
    public static Class<?> toWrapper(@NonNull Class<?> clazz) {
        if (clazz == Integer.TYPE) return Integer.class;
        if (clazz == Long.TYPE) return Long.class;
        if (clazz == Boolean.TYPE) return Boolean.class;
        if (clazz == Byte.TYPE) return Byte.class;
        if (clazz == Character.TYPE) return Character.class;
        if (clazz == Float.TYPE) return Float.class;
        if (clazz == Double.TYPE) return Double.class;
        if (clazz == Short.TYPE) return Short.class;
        if (clazz == Void.TYPE) return Void.class;
        return clazz;
    }

}
