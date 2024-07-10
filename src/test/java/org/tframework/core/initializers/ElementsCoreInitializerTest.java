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
package org.tframework.core.initializers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.Application;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.ElementsInitializationInput;
import org.tframework.core.elements.ElementsInitializationProcess;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.scanner.ElementContextBundle;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainerFactory;

@ExtendWith(MockitoExtension.class)
class ElementsCoreInitializerTest {

    @Mock
    private ElementContext elementContext;

    @Mock
    private ElementsInitializationProcess elementsInitializationProcess;

    private ElementsCoreInitializer elementsCoreInitializer;

    @BeforeEach
    void setUp() {
        elementsCoreInitializer = new ElementsCoreInitializer(elementsInitializationProcess);
    }

    @Test
    public void shouldPerformElementsCoreInitialization() {
        var application = Application.builder()
                .propertiesContainer(PropertiesContainerFactory.empty())
                .profilesContainer(ProfilesContainer.empty())
                .build();

        var input = ElementsInitializationInput.builder()
                .application(application)
                .rootClass(this.getClass())
                .preConstructedElementData(Set.of())
                .build();

        var expectedElements = ElementsContainer.fromElementContexts(List.of(elementContext));
        when(elementsInitializationProcess.initialize(eq(input), any(ElementContextBundle.class)))
                .thenReturn(expectedElements);

        var actualElements = elementsCoreInitializer.initialize(input);
        assertEquals(expectedElements, actualElements);
    }

}
