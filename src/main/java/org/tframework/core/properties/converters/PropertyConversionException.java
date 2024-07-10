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
