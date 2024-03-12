/* Licensed under Apache-2.0 2024. */
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
