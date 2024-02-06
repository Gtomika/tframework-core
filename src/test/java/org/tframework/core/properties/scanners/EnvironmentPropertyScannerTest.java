/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.scanners;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.properties.parsers.PropertyParsingUtils;
import org.tframework.core.readers.EnvironmentVariableReader;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.tframework.core.properties.scanners.EnvironmentPropertyScanner.PROPERTY_VARIABLE_PREFIX;

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
