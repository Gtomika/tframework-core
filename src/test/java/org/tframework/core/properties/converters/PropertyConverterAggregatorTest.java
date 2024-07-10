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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.tframework.core.properties.PropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PropertyConverterAggregatorTest {

    private final PropertyValue propertyValue = new SinglePropertyValue("test");

    @Mock
    private PropertyConverter<String> stringConverter;

    @Mock
    private PropertyConverter<Integer> intConverter;

    @Mock
    private PropertyConverter<Child> childConverter;

    private PropertyConverterAggregator aggregator;

    @BeforeEach
    public void setUp() {
        when(stringConverter.getType()).thenReturn(String.class);
        when(intConverter.getType()).thenReturn(Integer.class);
        when(childConverter.getType()).thenReturn(Child.class);

        aggregator = PropertyConverterAggregator.usingConverters(
                List.of(stringConverter, intConverter, childConverter)
        );
    }

    @Test
    public void shouldConvertPropertyValue_ifConverterExistsWithExactType() {
        when(stringConverter.convert(propertyValue)).thenReturn("test");

        String convertedValue = aggregator.convert(propertyValue, String.class);
        assertEquals("test", convertedValue);
    }

    @Test
    public void shouldConvertPropertyValue_ifConverterExistsWithExactPrimitiveType() {
        when(intConverter.convert(propertyValue)).thenReturn(1);

        int convertedValue = aggregator.convert(propertyValue, int.class);
        assertEquals(1, convertedValue);
    }

    @Test
    public void shouldConvertPropertyValue_ifConverterExistsWithAssignableType() {
        Child expected = new Child();
        when(childConverter.convert(propertyValue)).thenReturn(expected);

        //Child is assignable to Parent. There is no exact Parent converter, so Child converter is used
        Parent convertedValue = aggregator.convert(propertyValue, Parent.class);
        assertEquals(expected, convertedValue);
    }

    @Test
    public void shouldThrowException_ifNoMatchingConverterExists() {
        var exception = assertThrows(PropertyConverterNotFoundException.class,
                () -> aggregator.convert(propertyValue, File.class));
        assertEquals(
                exception.getMessageTemplate().formatted(File.class.getName()),
                exception.getMessage()
        );
    }

    static class Parent {}

    static class Child extends Parent {}

}
