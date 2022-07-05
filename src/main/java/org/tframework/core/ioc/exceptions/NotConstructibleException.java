package org.tframework.core.ioc.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

/**
 * Exception thrown when the IoC is unable to create an instance of a managed entity.
 */
public class NotConstructibleException extends TFrameworkRuntimeException {

    public NotConstructibleException(Class<?> clazz) {
        super(String.format("IoC cannot construct instance of class '%s'", clazz.getName()));
    }

    public NotConstructibleException(Class<?> clazz, Throwable cause) {
        super(String.format("IoC cannot construct instance of class '%s'", clazz.getName()), cause);
    }

    public NotConstructibleException(String name) {
        super(String.format("IoC cannot construct instance of managed entity with name '%s'", name));
    }
}
