/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.properties.SinglePropertyValue;

@ExtendWith(MockitoExtension.class)
class PropertyConverterTest {

    @Mock
    private PropertyConverter<Integer> propertyConverter;

    @Test
    public void shouldConvertProperty_whenConvertInternalIsSuccessful() throws Exception {
        var propertyValue = new SinglePropertyValue("1");
        int expectedConvertedValue = 1;
        when(propertyConverter.convertInternal(propertyValue)).thenReturn(expectedConvertedValue);
        when(propertyConverter.convert(propertyValue)).thenCallRealMethod();

        int convertedValue = propertyConverter.convert(propertyValue);

        assertEquals(expectedConvertedValue, convertedValue);
    }

    @Test
    public void shouldThrowPropertyConversionException_whenConvertInternalThrowsException() throws Exception {
        var propertyValue = new SinglePropertyValue("a");
        var expectedCause = new Exception();
        when(propertyConverter.convertInternal(propertyValue)).thenThrow(expectedCause);
        when(propertyConverter.convert(propertyValue)).thenCallRealMethod();
        when(propertyConverter.getType()).thenReturn(Integer.class);

        var conversionException = assertThrows(PropertyConversionException.class, () -> propertyConverter.convert(propertyValue));

        assertEquals(
                conversionException.getMessageTemplate().formatted(propertyValue, propertyConverter.getType().getName()),
                conversionException.getMessage()
        );
        assertEquals(expectedCause, conversionException.getCause());
    }


}
