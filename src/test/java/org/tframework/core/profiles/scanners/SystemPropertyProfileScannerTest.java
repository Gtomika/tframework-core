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
