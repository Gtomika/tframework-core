/* Licensed under Apache-2.0 2022. */
package org.tframework.core.exceptions;

/** Base class for all unchecked exceptions in TFramework. */
public class TFrameworkException extends RuntimeException {

    public TFrameworkException(String message) {
        super(message);
    }

    public TFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

}
