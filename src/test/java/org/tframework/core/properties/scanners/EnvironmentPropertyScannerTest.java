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
import static org.tframework.core.properties.scanners.EnvironmentPropertyScanner.PROPERTY_VARIABLE_PREFIX;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.properties.parsers.PropertyParsingUtils;
import org.tframework.core.readers.EnvironmentVariableReader;

@ExtendWith(MockitoExtension.class)
class EnvironmentPropertyScannerTest {

    @Mock
    private EnvironmentVariableReader environmentVariableReader;

    private EnvironmentPropertyScanner environmentPropertyScanner;

    @BeforeEach
    void setUp() {
        environmentPropertyScanner = new EnvironmentPropertyScanner(environmentVariableReader);
    }

    @Test
    public void shouldDetectEnvironmentVariablesThatHoldProperties_andReturnRawProperties() {
        when(environmentVariableReader.getAllVariableNames()).thenReturn(Set.of(
            PROPERTY_VARIABLE_PREFIX + "cool.prop",
            "TFRAMEWORK_PROFILES=test,debug",
            PROPERTY_VARIABLE_PREFIX + "test.prop"
        ));
        when(environmentVariableReader.readVariable(PROPERTY_VARIABLE_PREFIX + "cool.prop")).thenReturn("cool");
        when(environmentVariableReader.readVariable(PROPERTY_VARIABLE_PREFIX + "test.prop")).thenReturn("test");

        var actualRawProperties = environmentPropertyScanner.scanProperties();
        var expectedRawProperties = List.of(
                "test.prop" + PropertyParsingUtils.PROPERTY_NAME_VALUE_SEPARATOR + "test",
                "cool.prop" + PropertyParsingUtils.PROPERTY_NAME_VALUE_SEPARATOR + "cool"
        );
        assertTrue(
                expectedRawProperties.containsAll(actualRawProperties) &&
                        actualRawProperties.containsAll(expectedRawProperties)
        );
    }
}
