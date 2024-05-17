/* Licensed under Apache-2.0 2024. */
package org.tframework.test.properties.explicit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.core.properties.parsers.PropertyParsingUtils;
import org.tframework.core.properties.scanners.CliArgumentPropertyScanner;
import org.tframework.core.utils.CliUtils;
import org.tframework.test.commons.annotations.SetCommandLineArguments;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@SetCommandLineArguments(
        CliArgumentPropertyScanner.PROPERTY_ARGUMENT_KEY + CliUtils.CLI_KEY_VALUE_SEPARATOR +
                "integration-test.custom.property" + PropertyParsingUtils.PROPERTY_NAME_VALUE_SEPARATOR + "value"
)
@IsolatedTFrameworkTest
public class ExplicitPropertyAsCliArgumentTest {

    @Test
    public void shouldPickUpProperty_fromCommandLineArguments(
            @InjectProperty("integration-test.custom.property") String customProperty
    ) {
        assertEquals("value", customProperty);
    }

}
