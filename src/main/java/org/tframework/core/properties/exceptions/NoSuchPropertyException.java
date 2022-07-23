package org.tframework.core.properties.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

/**
 * Thrown when a property is not found.
 */
public class NoSuchPropertyException extends TFrameworkRuntimeException {

    public NoSuchPropertyException(String propertyName, String cause) {
        super(String.format("Property with name '%s' not found. Cause: %s", propertyName, cause));
    }

    public NoSuchPropertyException(String propertyName, Throwable cause) {
        super(String.format("Property with name '%s' not found.", propertyName), cause);
    }
}
