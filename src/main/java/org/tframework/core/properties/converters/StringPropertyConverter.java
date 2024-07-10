/*
Copyright 2023 Tamas Gaspar

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
 * A {@link PropertyConverter} which converts to {@link String} values. Allows for broad conversion by
 * using {@link String#valueOf(Object)}: any property can be converted to a string.
 */
@Element
public class StringPropertyConverter implements PropertyConverter<String> {

    @Override
    public String convert(PropertyValue propertyValue) {
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
