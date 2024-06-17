/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.converters;

import org.tframework.core.elements.annotations.Element;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

/**
 * A {@link PropertyConverter} that converts to integer, as specified in
 * {@link Integer#parseInt(String)}.
 */
@Element
public class IntegerPropertyConverter implements PropertyConverter<Integer> {

    @Override
    public Integer convert(PropertyValue propertyValue) {
        try {
            return toInteger(propertyValue);
        } catch (NumberFormatException e) {
            throw PropertyConversionException.builder()
                    .propertyValue(propertyValue)
                    .type(getType())
                    .cause(e)
                    .build();
        }
    }

    private Integer toInteger(PropertyValue propertyValue) {
        return switch(propertyValue) {
            case SinglePropertyValue(var value) -> Integer.parseInt(value);
            case ListPropertyValue lpv -> throw PropertyConversionException.builder()
                    .propertyValue(propertyValue)
                    .type(getType())
                    .build();
        };
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}
