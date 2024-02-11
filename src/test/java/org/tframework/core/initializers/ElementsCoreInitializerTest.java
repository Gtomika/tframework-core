/* Licensed under Apache-2.0 2024. */
package org.tframework.core.initializers;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        var input = ElementsInitializationInput.builder()
                .application(Application.empty())
                .rootClass(this.getClass())
                .preConstructedElementData(Set.of())
                .build();
        var expectedElements = ElementsContainer.fromElementContexts(List.of(elementContext));
        when(elementsInitializationProcess.initialize(input)).thenReturn(expectedElements);

        var actualElements = elementsCoreInitializer.initialize(input);
        assertEquals(expectedElements, actualElements);
    }

}
