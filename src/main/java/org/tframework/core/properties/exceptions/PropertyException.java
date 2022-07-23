package org.tframework.core.properties.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

public class PropertyException extends TFrameworkRuntimeException {

    public PropertyException(String propertyName, String cause) {
        super(String.format("Error when processing property '%s'. Cause: %s", propertyName, cause));
    }

    public PropertyException(String propertyName, Throwable cause) {
        super(String.format("Error when processing property '%s'.", propertyName), cause);
    }
}
