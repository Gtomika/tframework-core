/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import java.lang.reflect.Method;
import org.tframework.core.elements.annotations.Element;

@Element
public class SimpleMethodInvoker implements MethodInvoker {

    @Override
    public void invokeMethodWithNoParametersAndIgnoreResult(
            Object instance,
            Method method
    ) throws MethodInvocationException {
        try {
            method.invoke(instance);
        } catch (Exception e) {
            throw new MethodInvocationException(method, instance.getClass(), e);
        }
    }

    @Override
    public void invokeMethodWithOneParameterAndIgnoreResult(
            Object instance,
            Method method,
            Object parameter
    ) throws MethodInvocationException {
        try {
            method.invoke(instance, parameter);
        } catch (Exception e) {
            throw new MethodInvocationException(method, instance.getClass(), e);
        }
    }
}
