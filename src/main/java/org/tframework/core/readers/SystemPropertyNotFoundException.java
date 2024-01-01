/* Licensed under Apache-2.0 2024. */
package org.tframework.core.readers;

import org.tframework.core.TFrameworkException;

/**
 * Thrown when a {@link SystemPropertyReader} cannot find a system property.
 */
public class SystemPropertyNotFoundException extends TFrameworkException {

    private static final String TEMPLATE = "System property '%s' not found.";

    public SystemPropertyNotFoundException(String systemPropertyName) {
        super(TEMPLATE.formatted(systemPropertyName));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }

}
