/*
Copyright 2024 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
