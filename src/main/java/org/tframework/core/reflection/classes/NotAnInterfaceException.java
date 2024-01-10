/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.classes;

import org.tframework.core.TFrameworkException;

/**
 * This exception is thrown if a {@link Class} should be an interface, but was not.
 */
public class NotAnInterfaceException extends TFrameworkException {

    private static final String TEMPLATE = "The class '%s' is not an interface";

    public NotAnInterfaceException(Class<?> clazz) {
        super(TEMPLATE.formatted(clazz.getName()));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
