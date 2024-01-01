/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.scanners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tframework.core.properties.scanners.CliArgumentPropertyFileScanner.PROPERTY_FILE_ARGUMENT_KEY;
import static org.tframework.core.utils.CliUtils.CLI_KEY_VALUE_SEPARATOR;

import java.util.List;
import org.junit.jupiter.api.Test;

class CliArgumentPropertyFileScannerTest {

    @Test
    public void shouldReturnEmptySet_whenNoPropertyFileArgumentIsProvided() {
        String[] args = new String[] {"--foo=bar", "testArg"};
        CliArgumentPropertyFileScanner scanner = new CliArgumentPropertyFileScanner(args);
        assertTrue(scanner.scan().isEmpty());
    }

    @Test
    public void shouldReturnEmptySet_whenPropertyFileArgumentIsProvided_butNoValue() {
        String[] args = new String[] {"--foo=bar", "testArg", PROPERTY_FILE_ARGUMENT_KEY+CLI_KEY_VALUE_SEPARATOR};
        CliArgumentPropertyFileScanner scanner = new CliArgumentPropertyFileScanner(args);
        assertTrue(scanner.scan().isEmpty());
    }

    @Test
    public void shouldReturnPropertyFiles_whenPropertyFileArgumentIsProvided() {
        String[] args = new String[] {
                "--foo=bar",
                "testArg",
                PROPERTY_FILE_ARGUMENT_KEY+CLI_KEY_VALUE_SEPARATOR+"custom-properties.yaml,special-properties.yaml",
                PROPERTY_FILE_ARGUMENT_KEY+CLI_KEY_VALUE_SEPARATOR+"props/custom-properties.yaml"
        };
        CliArgumentPropertyFileScanner scanner = new CliArgumentPropertyFileScanner(args);
        var expected = List.of("custom-properties.yaml", "special-properties.yaml", "props/custom-properties.yaml");
        assertEquals(expected, scanner.scan());
    }

}
