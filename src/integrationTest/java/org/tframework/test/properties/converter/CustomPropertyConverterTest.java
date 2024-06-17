/* Licensed under Apache-2.0 2024. */
package org.tframework.test.properties.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertyValue;
import org.tframework.core.properties.SinglePropertyValue;
import org.tframework.core.properties.converters.PropertyConversionException;
import org.tframework.core.properties.converters.PropertyConverter;
import org.tframework.core.properties.filescanners.CliArgumentPropertyFileScanner;
import org.tframework.core.utils.CliUtils;
import org.tframework.test.commons.annotations.SetCommandLineArguments;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@SetCommandLineArguments(
        CliArgumentPropertyFileScanner.PROPERTY_FILE_ARGUMENT_KEY + CliUtils.CLI_KEY_VALUE_SEPARATOR + "custom-properties.yaml"
)
@IsolatedTFrameworkTest
public class CustomPropertyConverterTest {

    @AllArgsConstructor
    public static class MyDataHolder {
        private String data;
    }

    @Element
    public static class MyDataHolderConverter implements PropertyConverter<MyDataHolder> {

        @Override
        public MyDataHolder convert(PropertyValue propertyValue) {
            return switch (propertyValue) {
                case SinglePropertyValue spv -> new MyDataHolder(spv.value());
                case ListPropertyValue lpv -> throw PropertyConversionException.builder()
                        .propertyValue(propertyValue)
                        .type(MyDataHolder.class)
                        .build();
            };
        }

        @Override
        public Class<MyDataHolder> getType() {
            return MyDataHolder.class;
        }
    }

    @InjectProperty("integration-test.custom.property") // defined in file custom-properties.yaml
    private MyDataHolder customProperty;

    @Test
    public void shouldUseCustomPropertyConverter() {
        //"value" is the value of the property "integration-test.custom.property" in custom-properties.yaml
        assertEquals("value", customProperty.data);
    }
}
