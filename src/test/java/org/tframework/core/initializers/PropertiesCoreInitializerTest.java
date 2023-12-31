/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.PropertiesInitializationInput;
import org.tframework.core.properties.PropertiesInitializationProcess;
import org.tframework.core.properties.SinglePropertyValue;

@ExtendWith(MockitoExtension.class)
class PropertiesCoreInitializerTest {

    @Mock
    private PropertiesInitializationProcess propertiesInitializationProcess;

    private PropertiesCoreInitializer propertiesCoreInitializer;

    @BeforeEach
    public void setUp() {
        propertiesCoreInitializer = new PropertiesCoreInitializer(propertiesInitializationProcess);
    }

    @Test
    void shouldInitializeProperties() {
        PropertiesContainer expectedProperties = PropertiesContainer.fromProperties(Map.of(
                "property1", new SinglePropertyValue("value1"),
                "property2", new SinglePropertyValue("value2"),
                "property3", new SinglePropertyValue("value3")
        ));
        when(propertiesInitializationProcess.initialize(any())).thenReturn(expectedProperties);

        var input = PropertiesInitializationInput.builder().build();
        PropertiesContainer actualProperties = propertiesCoreInitializer.initialize(input);

        assertEquals(expectedProperties, actualProperties);
    }
}
