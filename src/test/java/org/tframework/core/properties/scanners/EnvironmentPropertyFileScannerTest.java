/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.scanners;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.tframework.core.properties.scanners.EnvironmentPropertyFileScanner.PROPERTY_FILES_ENVIRONMENT_VARIABLE;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.readers.EnvironmentVariableNotFoundException;
import org.tframework.core.readers.EnvironmentVariableReader;

@ExtendWith(MockitoExtension.class)
class EnvironmentPropertyFileScannerTest {

    @Mock
    private EnvironmentVariableReader reader;

    private EnvironmentPropertyFileScanner scanner;

    @BeforeEach
    void setUp() {
        scanner = new EnvironmentPropertyFileScanner(reader);
    }

    @Test
    public void shouldScanPropertyFiles_ifEnvironmentVariableIsSet() {
        String propertyFilesRaw = "file1,file2,file3";
        when(reader.readVariable(PROPERTY_FILES_ENVIRONMENT_VARIABLE))
                .thenReturn(propertyFilesRaw);

        var propertyFiles = scanner.scan();

        assertEquals(List.of("file1", "file2", "file3"), propertyFiles);
    }

    @Test
    public void shouldScanEmptyPropertyFiles_ifEnvironmentVariableIsNotSet() {
        when(reader.readVariable(PROPERTY_FILES_ENVIRONMENT_VARIABLE))
                .thenThrow(new EnvironmentVariableNotFoundException(PROPERTY_FILES_ENVIRONMENT_VARIABLE));

        var propertyFiles = scanner.scan();

        assertTrue(propertyFiles.isEmpty());
    }
}
