package org.tframework.core.properties.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

public class PropertyFileNotFoundException extends TFrameworkRuntimeException {

    public PropertyFileNotFoundException(String message) {
        super(message);
    }

    public PropertyFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
