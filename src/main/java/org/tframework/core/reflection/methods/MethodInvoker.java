/*
Copyright 2024 Tamas Gaspar

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
