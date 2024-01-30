/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import org.junit.jupiter.api.Test;

class PropertyConvertersFactoryTest {

     @Test
     void shouldGetConverter() {
         PropertyConverter<String> converter = PropertyConvertersFactory.getConverter(String.class);
         assertEquals(StringPropertyConverter.class, converter.getClass());
     }

     @Test
     void shouldThrowException_ifConverterByTypeDoesNotExist() {
         var exception = assertThrows(PropertyConverterNotFoundException.class, () -> {
             PropertyConvertersFactory.getConverter(File.class);
         });
         assertEquals(
                 exception.getMessageTemplate().formatted(File.class.getName()),
                 exception.getMessage()
         );
     }

}
