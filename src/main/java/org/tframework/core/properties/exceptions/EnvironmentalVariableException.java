package org.tframework.core.properties.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

public class EnvironmentalVariableException extends TFrameworkRuntimeException {

    public EnvironmentalVariableException(String message) {
        super(message);
    }

    public EnvironmentalVariableException(String message, Throwable cause) {
        super(message, cause);
    }
}
