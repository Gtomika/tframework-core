/* Licensed under Apache-2.0 2024. */
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
