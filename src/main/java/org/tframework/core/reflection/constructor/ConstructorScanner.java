/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.constructor;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * The constructor scanner finds constructors of a class.
 * @see ConstructorFilter
 */
public interface ConstructorScanner {

    /**
     * Returns all constructors of the given class.
     * @param clazz The class whose constructors are being scanned.
     */
    Set<Constructor<?>> getAllConstructors(Class<?> clazz);

}
