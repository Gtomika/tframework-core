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
package org.tframework.core.properties.filescanners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tframework.core.properties.filescanners.CliArgumentPropertyFileScanner.PROPERTY_FILE_ARGUMENT_KEY;
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
