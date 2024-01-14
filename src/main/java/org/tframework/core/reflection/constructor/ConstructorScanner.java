/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.constructor;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * The constructor scanner finds constructors of a class.
 * @param <T> The type of the class whose constructors are being scanned.
 * @see ConstructorFilter
 */
public interface ConstructorScanner<T> {

    /**
     * Returns all constructors of the given class.
     * @param clazz The class whose constructors are being scanned.
     */
    Set<Constructor<T>> getAllConstructors(Class<T> clazz);

}
