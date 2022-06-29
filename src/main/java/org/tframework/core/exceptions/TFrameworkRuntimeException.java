package org.tframework.core.exceptions;

/**
 * Base class for all unchecked exceptions in TFramework.
 */
public class TFrameworkRuntimeException extends RuntimeException {

    public TFrameworkRuntimeException(String message) {
        super(message);
    }

    public TFrameworkRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
