/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import org.tframework.core.TFrameworkException;

/**
 * Thrown when attempting to retrieve a property which does not exist.
 */
public class PropertyNotFoundException extends TFrameworkException {

    private static final String TEMPLATE = "Property with name '%s' does not exist";

    public PropertyNotFoundException(String propertyName) {
        super(TEMPLATE.formatted(propertyName));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
