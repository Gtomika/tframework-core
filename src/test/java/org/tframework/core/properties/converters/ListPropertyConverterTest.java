/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

class ListPropertyConverterTest {

    private final ListPropertyConverter listPropertyConverter = new ListPropertyConverter();

    @Test
    public void shouldConvertSinglePropertyValue() {
        String expectedString = "test";
        var spv = new SinglePropertyValue(expectedString);
        List<String> convertedValue = listPropertyConverter.convert(spv);
        assertEquals(List.of(expectedString), convertedValue);
    }

    @Test
    public void shouldConvertSinglePropertyValue_whenSingleValueIsNull() {
        var spv = new SinglePropertyValue(null);
        List<String> convertedValue = listPropertyConverter.convert(spv);
        assertNull(convertedValue.getFirst());
    }

    @Test
    public void shouldConvertListPropertyValue() {
        var expectedList = List.of("test1", "test2");
        var lpv = new ListPropertyValue(expectedList);
        List<String> convertedValue = listPropertyConverter.convert(lpv);
        assertEquals(expectedList, convertedValue);
    }

}
