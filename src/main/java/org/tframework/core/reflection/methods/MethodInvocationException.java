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
import org.tframework.core.TFrameworkException;

/**
 * Thrown when {@link MethodInvoker} encounters a problem.
 */
public class MethodInvocationException extends TFrameworkException {

    private static final String TEMPLATE = "Method '%s' of class '{}' could not be invoked";

    public MethodInvocationException(Method method, Class<?> clazz, Exception cause) {
        super(TEMPLATE.formatted(method.getName(), clazz.getName()), cause);
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
