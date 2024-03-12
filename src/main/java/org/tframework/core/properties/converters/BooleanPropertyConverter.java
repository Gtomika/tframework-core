/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.converters;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

/**
 * A {@link PropertyConverter} which converts to {@link Boolean}, as specified
 * by the rules of {@link Boolean#valueOf(String)}.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class BooleanPropertyConverter implements PropertyConverter<Boolean> {

    @Override
    public Boolean convert(PropertyValue propertyValue) {
        return switch(propertyValue) {
            case SinglePropertyValue(var value) -> Boolean.valueOf(value);
            case ListPropertyValue lpv -> throw PropertyConversionException.builder()
                    .propertyValue(propertyValue)
                    .type(getType())
                    .build();
        };
    }

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }
}
