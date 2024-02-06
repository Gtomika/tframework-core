/* Licensed under Apache-2.0 2024. */
package org.tframework.core.profiles.scanners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.tframework.core.profiles.scanners.SystemPropertyProfileScanner.PROFILES_SYSTEM_PROPERTY;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.readers.SystemPropertyNotFoundException;
import org.tframework.core.readers.SystemPropertyReader;

@ExtendWith(MockitoExtension.class)
class SystemPropertyProfileScannerTest {

    @Mock
    private SystemPropertyReader systemPropertyReader;

    private SystemPropertyProfileScanner scanner;

    @BeforeEach
    public void setUp() {
        scanner = new SystemPropertyProfileScanner(systemPropertyReader);
    }

    @Test
    public void shouldScanProfilesFromSystemProperty_ifProvided() {
        when(systemPropertyReader.readSystemProperty(PROFILES_SYSTEM_PROPERTY))
                .thenReturn("dev,db");

        var profiles = scanner.scan();

        assertEquals(Set.of("dev", "db"), profiles);
    }

    @Test
    public void shouldScanProfilesFromSystemProperty_ifNotProvided() {
        when(systemPropertyReader.readSystemProperty(PROFILES_SYSTEM_PROPERTY))
                .thenThrow(new SystemPropertyNotFoundException(PROFILES_SYSTEM_PROPERTY));

        var profiles = scanner.scan();

        assertEquals(Set.of(), profiles);
    }

}
