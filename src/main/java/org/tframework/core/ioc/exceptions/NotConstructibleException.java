package org.tframework.core.ioc.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

public class NotConstructibleException extends TFrameworkRuntimeException {

    public NotConstructibleException(Class<?> clazz) {
        super(String.format("IoC cannot construct instance of class '%s'", clazz.getName()));
    }

    public NotConstructibleException(Class<?> clazz, Throwable cause) {
        super(String.format("IoC cannot construct instance of class '%s'", clazz.getName()), cause);
    }
}
