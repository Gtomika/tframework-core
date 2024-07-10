/*
Copyright 2023 Tamas Gaspar

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
package org.tframework.core.initializers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.PropertiesContainerFactory;
import org.tframework.core.properties.PropertiesInitializationInput;
import org.tframework.core.properties.PropertiesInitializationProcess;
import org.tframework.core.properties.Property;
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
        PropertiesContainer expectedProperties = PropertiesContainerFactory.fromProperties(List.of(
                new Property("property1", new SinglePropertyValue("value1")),
                new Property("property2", new SinglePropertyValue("value2")),
                new Property("property3", new SinglePropertyValue("value3"))
        ));
        when(propertiesInitializationProcess.initialize(any())).thenReturn(expectedProperties);

        var input = PropertiesInitializationInput.builder().build();
        PropertiesContainer actualProperties = propertiesCoreInitializer.initialize(input);

        assertEquals(expectedProperties, actualProperties);
    }
}
