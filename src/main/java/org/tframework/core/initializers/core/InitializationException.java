/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers.core;

import org.tframework.core.TFrameworkException;

/**
 * Thrown during the framework initialization, if an exception prevented the initialization
 * process. The exception responsible is saved as the cause of this one.
 */
public class InitializationException extends TFrameworkException {

	private static final String TEMPLATE = "Exception during framework initialization: '%s'";

	public InitializationException(Exception cause) {
		super(TEMPLATE.formatted(cause.getClass().getName()), cause);
	}

	@Override
	public String getMessageTemplate() {
		return TEMPLATE;
	}
}
