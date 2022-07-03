package org.tframework.core.ioc.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

/**
 * Top level exception thrown by the IoC classes to indicate that
 * some IoC operation could not be performed. The cause exception
 * can provide details.
 */
public class IocException extends TFrameworkRuntimeException {

    public IocException(String message, Throwable cause) {
        super(message, cause);
    }

}
