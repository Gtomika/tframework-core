/* Licensed under Apache-2.0 2022. */
package org.tframework.core.exceptions;

/**
 *  Base class for all unchecked exceptions in TFramework. All exceptions declared in the framework
 *  should be subclasses of this one.
 */
public abstract class TFrameworkException extends RuntimeException {

    /**
     * Create an exception with a message.
     * @param message Message that should come from {@link #getMessageTemplate()} with formatting applied.
     */
    public TFrameworkException(String message) {
        super(message);
    }

    /**
     * Create an exception with a message and a cause.
     * @param message Message that should come from {@link #getMessageTemplate()} with formatting applied.
     * @param cause Another exception that was the direct cause of this one.
     */
    public TFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Gets the message template of the exception. This must have the same rules as defined by
     * {@link String#format(String, Object...)}. It is allowed to have no placeholders in the template.
     */
    public abstract String getMessageTemplate();

}
