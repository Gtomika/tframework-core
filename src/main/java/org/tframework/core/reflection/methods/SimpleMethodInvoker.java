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
