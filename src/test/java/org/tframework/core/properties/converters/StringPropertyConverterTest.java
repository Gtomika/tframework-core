/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

class StringPropertyConverterTest {

    private final StringPropertyConverter converter = new StringPropertyConverter();

    @Test
    public void shouldConvertSinglePropertyValueToString() {
        String expectedValue = "test";
        SinglePropertyValue spv = new SinglePropertyValue(expectedValue);
        String actualValue = converter.convert(spv);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void shouldConvertNullSinglePropertyValueToString() {
        SinglePropertyValue spv = new SinglePropertyValue(null);
        String actualValue = converter.convert(spv);
        assertNull(actualValue);
    }

    @Test
    public void shouldConvertCollectionValueToString() {
        var expectedValues = List.of("test1", "test2");
        var cpv = new ListPropertyValue(expectedValues);
        String actualValues = converter.convert(cpv);
        assertEquals(expectedValues.toString(), actualValues);
    }

}
