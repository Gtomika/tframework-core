/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.converters;

import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

/**
 * A {@link PropertyConverter} which converts to {@link String} values. Allows for broad conversion by
 * using {@link String#valueOf(Object)}: any property can be converted to a string.
 */
public class StringPropertyConverter extends PropertyConverter<String> {

    @Override
    protected String convertInternal(PropertyValue propertyValue) {
        return switch(propertyValue) {
            case SinglePropertyValue spv -> spv.value();
            case ListPropertyValue cpv -> String.valueOf(cpv.values());
        };
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}