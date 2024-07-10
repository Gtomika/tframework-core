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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

public class IntegerPropertyConverterTest {

    private final IntegerPropertyConverter converter = new IntegerPropertyConverter();

    @Test
    public void shouldConvertValidPropertyValue() {
        var propertyValue = new SinglePropertyValue("2");
        assertEquals(2, converter.convert(propertyValue));
    }

    @Test
    public void shouldThrowExceptionForListPropertyValue() {
        var propertyValue = new ListPropertyValue(List.of("a", "1"));
        var exception = assertThrows(PropertyConversionException.class, () -> converter.convert(propertyValue));

        assertEquals(
                exception.getMessageTemplate().formatted(propertyValue, Integer.class.getName()),
                exception.getMessage()
        );
    }

    @Test
    public void shouldThrowExceptionForNotIntegerPropertyValue() {
        var propertyValue = new SinglePropertyValue("not an int");
        var exception = assertThrows(PropertyConversionException.class, () -> converter.convert(propertyValue));

        assertEquals(
                exception.getMessageTemplate().formatted(propertyValue, Integer.class.getName()),
                exception.getMessage()
        );
        assertInstanceOf(NumberFormatException.class, exception.getCause());
    }
}
