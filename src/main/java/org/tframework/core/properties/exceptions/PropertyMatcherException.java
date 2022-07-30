package org.tframework.core.properties.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

public class PropertyMatcherException extends TFrameworkRuntimeException {

    public PropertyMatcherException(String message) {
        super(message);
    }

    public PropertyMatcherException(String message, Throwable cause) {
        super(message, cause);
    }
}
