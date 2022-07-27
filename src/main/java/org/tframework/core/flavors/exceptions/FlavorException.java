package org.tframework.core.flavors.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

public class FlavorException extends TFrameworkRuntimeException {

    public FlavorException(String message) {
        super(message);
    }

    public FlavorException(String message, Throwable cause) {
        super(message, cause);
    }
}
