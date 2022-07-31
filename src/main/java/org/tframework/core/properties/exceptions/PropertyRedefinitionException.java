package org.tframework.core.properties.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

/**
 * Thrown when an existing property is redefined in an invalid way.
 */
public class PropertyRedefinitionException extends TFrameworkRuntimeException {

    public PropertyRedefinitionException(String message) {
        super(message);
    }

    public PropertyRedefinitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
