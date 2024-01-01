/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.scanners;

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
        when(systemPropertyReader.readProperty(SystemPropertyFileScanner.PROPERTY_FILES_SYSTEM_PROPERTY))
                .thenReturn(systemProperty);

        var propertyFiles = systemPropertyFileScanner.scan();

        assertEquals(List.of("custom-properties.yaml", "another-properties.yaml"), propertyFiles);
    }

    @Test
    public void shouldScanPropertyFiles_ifSystemPropertyIsProvided_withSpaces() {
        String systemProperty = "custom-properties.yaml, another-properties.yaml";
        when(systemPropertyReader.readProperty(SystemPropertyFileScanner.PROPERTY_FILES_SYSTEM_PROPERTY))
                .thenReturn(systemProperty);

        var propertyFiles = systemPropertyFileScanner.scan();

        assertEquals(List.of("custom-properties.yaml", "another-properties.yaml"), propertyFiles);
    }

    @Test
    public void shouldScanEmptyPropertyFiles_ifSystemPropertyIsNotProvided() {
        when(systemPropertyReader.readProperty(SystemPropertyFileScanner.PROPERTY_FILES_SYSTEM_PROPERTY))
                .thenThrow(new SystemPropertyNotFoundException(SystemPropertyFileScanner.PROPERTY_FILES_SYSTEM_PROPERTY));

        var propertyFiles = systemPropertyFileScanner.scan();

        assertEquals(List.of(), propertyFiles);
    }

}
