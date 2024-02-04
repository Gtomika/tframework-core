/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.scanners;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        assertEquals(List.of("some.cool.prop=123", "other.cool.prop=456"), actualProperties);
    }
}
