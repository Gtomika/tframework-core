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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

class StringListPropertyConverterTest {

    private final StringListPropertyConverter stringListPropertyConverter = new StringListPropertyConverter();

    @Test
    public void shouldConvertSinglePropertyValue() {
        String expectedString = "test";
        var spv = new SinglePropertyValue(expectedString);
        List<String> convertedValue = stringListPropertyConverter.convert(spv);
        assertEquals(List.of(expectedString), convertedValue);
    }

    @Test
    public void shouldConvertSinglePropertyValue_whenSingleValueIsNull() {
        var spv = new SinglePropertyValue(null);
        List<String> convertedValue = stringListPropertyConverter.convert(spv);
        assertNull(convertedValue.getFirst());
    }

    @Test
    public void shouldConvertListPropertyValue() {
        var expectedList = List.of("test1", "test2");
        var lpv = new ListPropertyValue(expectedList);
        List<String> convertedValue = stringListPropertyConverter.convert(lpv);
        assertEquals(expectedList, convertedValue);
    }

}
