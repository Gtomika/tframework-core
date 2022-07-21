package org.tframework.core.ioc.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

/**
 * Exception thrown when the IoC is unable to create an instance of a managed entity.
 */
public class NotConstructibleException extends TFrameworkRuntimeException {

    private static final String CONSTRUCT_HINT = "A managed class needs to have either a public constructor, or be " +
            "provided with a provider method.";

    public NotConstructibleException(Class<?> clazz) {
        super(String.format("IoC cannot construct instance of class '%s'. %s", clazz.getName(), CONSTRUCT_HINT));
    }

    public NotConstructibleException(Class<?> clazz, Throwable cause) {
        super(String.format("IoC cannot construct instance of class '%s'. %s", clazz.getName(), CONSTRUCT_HINT), cause);
    }

    public NotConstructibleException(String name) {
        super(String.format("IoC cannot construct instance of managed entity with name '%s'. %s", name, CONSTRUCT_HINT));
    }

    private NotConstructibleException(String customMessage, int placeholder) {
        super(customMessage);
    }

    /**
     * Can be used when no other constructor describes the reason of the exception.
     * @param customMessage Reason for the exception.
     */
    public static NotConstructibleException customNotConstructibleException(String customMessage) {
        return new NotConstructibleException(customMessage, 0);
    }
}
