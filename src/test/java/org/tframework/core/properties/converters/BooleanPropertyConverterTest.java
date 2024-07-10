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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

class BooleanPropertyConverterTest {

    private final BooleanPropertyConverter converter = new BooleanPropertyConverter();

    @Test
    public void shouldConvertPropertyValueToBoolean() {
        var propertyValue = new SinglePropertyValue("true");
        assertTrue(converter.convert(propertyValue));
    }

    @Test
    public void shouldThrowExceptionWhenConvertingListPropertyValue() {
        var propertyValue = new ListPropertyValue(List.of("true", "false"));
        assertThrows(PropertyConversionException.class, () -> converter.convert(propertyValue));
    }

}
