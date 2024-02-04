/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.scanners;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.utils.CliUtils;

class CliArgumentPropertyScannerTest {

    private CliArgumentPropertyScanner scanner;

    @Test
    public void shouldScanPropertiesFromArguments() {
        String[] args = new String[] {
            CliArgumentPropertyScanner.PROPERTY_ARGUMENT_KEY + CliUtils.CLI_KEY_VALUE_SEPARATOR + "some.cool.prop=123",
            "tframework.profiles=dev,test",
            CliArgumentPropertyScanner.PROPERTY_ARGUMENT_KEY + CliUtils.CLI_KEY_VALUE_SEPARATOR + "other.cool.prop=456"
        };
        scanner = new CliArgumentPropertyScanner(args);

        var actualProperties = scanner.scanProperties();
        var expectedProperties = List.of("some.cool.prop=123", "other.cool.prop=456");
        assertEquals(expectedProperties, actualProperties);
    }

}
