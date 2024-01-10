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
     * Finds all methods of the class or classes.
     */
    Set<Method> scanMethods();

}
