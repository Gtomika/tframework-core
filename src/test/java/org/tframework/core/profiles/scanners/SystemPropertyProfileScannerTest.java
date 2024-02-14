/* Licensed under Apache-2.0 2024. */
package org.tframework.core.profiles.scanners;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.readers.SystemPropertyReader;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.tframework.core.profiles.scanners.SystemPropertyProfileScanner.PROFILES_SYSTEM_PROPERTY;

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
        when(systemPropertyReader.getAllSystemPropertyNames())
                .thenReturn(Set.of(PROFILES_SYSTEM_PROPERTY, "some.other.property"));
        when(systemPropertyReader.readSystemProperty(PROFILES_SYSTEM_PROPERTY))
                .thenReturn("dev, db");

        var profiles = scanner.scan();

        assertEquals(Set.of("dev", "db"), profiles);
    }

    @Test
    public void shouldScanProfilesFromSystemProperty_ifNotProvided() {
        when(systemPropertyReader.getAllSystemPropertyNames())
                .thenReturn(Set.of("some.other.property"));

        var profiles = scanner.scan();

        assertEquals(Set.of(), profiles);
    }

}
