/* Licensed under Apache-2.0 2024. */
package org.tframework.properties.files;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.core.properties.filescanners.CliArgumentPropertyFileScanner;
import org.tframework.core.utils.CliUtils;
import org.tframework.test.commons.annotations.SetCommandLineArguments;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@SetCommandLineArguments(
        CliArgumentPropertyFileScanner.PROPERTY_FILE_ARGUMENT_KEY + CliUtils.CLI_KEY_VALUE_SEPARATOR + "custom-properties.yaml"
)
@IsolatedTFrameworkTest
public class CustomPropertyFileAsCliArgumentTest {

    @Test
    public void shouldPickUpCustomPropertiesFile_fromCommandLineArguments(
            @InjectProperty("integration-test.custom.property") String customProperty
    ) {
        assertEquals("value", customProperty);
    }

}
