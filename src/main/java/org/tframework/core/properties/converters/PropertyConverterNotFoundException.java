/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.converters;

import org.tframework.core.TFrameworkException;

/**
 * Thrown when a {@link PropertyConverter} cannot be found for a given type.
 */
public class PropertyConverterNotFoundException extends TFrameworkException {

    private static final String TEMPLATE = "No property converter found for type '%s'";

    PropertyConverterNotFoundException(Class<?> type) {
        super(TEMPLATE.formatted(type.getName()));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
