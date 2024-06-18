/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import java.lang.reflect.Method;

/**
 * Encapsulates method invocation logic and exception handling.
 */
public interface MethodInvoker {

    /**
     * Invokes the given simple, parameterless method, and ignores the return value, if there is one.
     * @param instance The object whose method must be invoked.
     * @param method The method to invoke: must be accessible, without parameters, and must belong to {@code instance}.
     * @throws MethodInvocationException If an underlying exception prevented invocation.
     */
    void invokeMethodWithNoParametersAndIgnoreResult(
            Object instance,
            Method method
    ) throws MethodInvocationException;

    /**
     * Invokes the given method which has exactly one parameter, and ignores the return value, if there is one.
     * @param instance The object whose method must be invoked.
     * @param method The method to invoke: must be accessible, with one parameter, and must belong to {@code instance}.
     * @throws MethodInvocationException If an underlying exception prevented invocation.
     */
    void invokeMethodWithOneParameterAndIgnoreResult(
            Object instance,
            Method method,
            Object parameter
    ) throws MethodInvocationException;
}
