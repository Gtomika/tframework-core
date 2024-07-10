/*
Copyright 2024 Tamas Gaspar

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.tframework.core.properties.filescanners.EnvironmentPropertyFileScanner.PROPERTY_FILES_ENVIRONMENT_VARIABLE;

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
