/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.scanners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        String[] args = new String[] {"--foo=bar", "testArg", "tframework.propertyFile="};
        CliArgumentPropertyFileScanner scanner = new CliArgumentPropertyFileScanner(args);
        assertTrue(scanner.scan().isEmpty());
    }

    @Test
    public void shouldReturnPropertyFiles_whenPropertyFileArgumentIsProvided() {
        String[] args = new String[] {
                "--foo=bar",
                "testArg",
                "tframework.propertyFile=custom-properties.yaml,special-properties.yaml",
                "tframework.propertyFile=props/custom-properties.yaml"
        };
        CliArgumentPropertyFileScanner scanner = new CliArgumentPropertyFileScanner(args);
        var expected = List.of("custom-properties.yaml", "special-properties.yaml", "props/custom-properties.yaml");
        assertEquals(expected, scanner.scan());
    }

}
