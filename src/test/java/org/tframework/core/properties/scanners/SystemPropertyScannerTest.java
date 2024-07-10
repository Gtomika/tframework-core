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
package org.tframework.core.properties.scanners;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.readers.SystemPropertyReader;

@ExtendWith(MockitoExtension.class)
class SystemPropertyScannerTest {

    @Mock
    private SystemPropertyReader systemPropertyReader;

    private SystemPropertyScanner systemPropertyScanner;

    @BeforeEach
    void setUp() {
        systemPropertyScanner = new SystemPropertyScanner(systemPropertyReader);
    }

    @Test
    public void shouldScanPropertiesFromSystemProperties() {
        var someSystemPropName =  SystemPropertyScanner.PROPERTY_PREFIX + "some.cool.prop";
        var otherSystemPropName =  SystemPropertyScanner.PROPERTY_PREFIX + "other.cool.prop";
        when(systemPropertyReader.getAllSystemPropertyNames()).thenReturn(Set.of(
            "tframework.profiles=dev,test",
           someSystemPropName,
           otherSystemPropName
        ));

        when(systemPropertyReader.readSystemProperty(someSystemPropName)).thenReturn("123");
        when(systemPropertyReader.readSystemProperty(otherSystemPropName)).thenReturn("456");

        var actualProperties = systemPropertyScanner.scanProperties();
        var expectedProperties = List.of("some.cool.prop=123", "other.cool.prop=456");
        assertTrue(
                actualProperties.containsAll(expectedProperties) &&
                        expectedProperties.containsAll(actualProperties)
        );
    }
}
