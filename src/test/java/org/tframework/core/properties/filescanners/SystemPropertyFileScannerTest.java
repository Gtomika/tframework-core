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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.readers.SystemPropertyNotFoundException;
import org.tframework.core.readers.SystemPropertyReader;

@ExtendWith(MockitoExtension.class)
class SystemPropertyFileScannerTest {

    @Mock
    private SystemPropertyReader systemPropertyReader;

    private SystemPropertyFileScanner systemPropertyFileScanner;

    @BeforeEach
    public void setUp() {
        systemPropertyFileScanner = new SystemPropertyFileScanner(systemPropertyReader);
    }

    @Test
    public void shouldScanPropertyFiles_ifSystemPropertyIsProvided() {
        String systemProperty = "custom-properties.yaml,another-properties.yaml";
        when(systemPropertyReader.readSystemProperty(SystemPropertyFileScanner.PROPERTY_FILES_SYSTEM_PROPERTY))
                .thenReturn(systemProperty);

        var propertyFiles = systemPropertyFileScanner.scan();

        assertEquals(List.of("custom-properties.yaml", "another-properties.yaml"), propertyFiles);
    }

    @Test
    public void shouldScanPropertyFiles_ifSystemPropertyIsProvided_withSpaces() {
        String systemProperty = "custom-properties.yaml, another-properties.yaml";
        when(systemPropertyReader.readSystemProperty(SystemPropertyFileScanner.PROPERTY_FILES_SYSTEM_PROPERTY))
                .thenReturn(systemProperty);

        var propertyFiles = systemPropertyFileScanner.scan();

        assertEquals(List.of("custom-properties.yaml", "another-properties.yaml"), propertyFiles);
    }

    @Test
    public void shouldScanEmptyPropertyFiles_ifSystemPropertyIsNotProvided() {
        when(systemPropertyReader.readSystemProperty(SystemPropertyFileScanner.PROPERTY_FILES_SYSTEM_PROPERTY))
                .thenThrow(new SystemPropertyNotFoundException(SystemPropertyFileScanner.PROPERTY_FILES_SYSTEM_PROPERTY));

        var propertyFiles = systemPropertyFileScanner.scan();

        assertEquals(List.of(), propertyFiles);
    }

}
