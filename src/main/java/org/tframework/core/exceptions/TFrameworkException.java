/* Licensed under Apache-2.0 2022. */
package org.tframework.core.exceptions;

/** Base class for all checked exceptions used in TFramework. */
public class TFrameworkException extends Exception {

    public TFrameworkException(String message) {
        super(message);
    }

    public TFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
