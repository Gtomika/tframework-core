/* Licensed under Apache-2.0 2024. */
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
