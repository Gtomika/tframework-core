/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * A method scanner is responsible for finding methods of a class or classes.
 * @see MethodScannersFactory
 */
public interface MethodScanner {

    /**
     * Finds the methods of the class.
     */
    Set<Method> scanMethods(Class<?> classToScan);

}
