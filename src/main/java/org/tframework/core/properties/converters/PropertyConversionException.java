/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.converters;

import lombok.Builder;
import org.tframework.core.TFrameworkException;
import org.tframework.core.properties.PropertyValue;

/**
 * Thrown when a {@link PropertyConverter} fails to convert to the desired type.
 */
@Builder
public class PropertyConversionException extends TFrameworkException {

    private static final String TEMPLATE = "Failed to convert property '%s' to type '%s'";

    private final PropertyValue propertyValue;
    private final Class<?> type;
    private final Exception cause;

    private PropertyConversionException(PropertyValue propertyValue, Class<?> type, Exception cause) {
        super(TEMPLATE.formatted(propertyValue, type.getName()), cause);
        this.propertyValue = propertyValue;
        this.type = type;
        this.cause = cause;
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
