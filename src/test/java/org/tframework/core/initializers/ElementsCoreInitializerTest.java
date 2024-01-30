package org.tframework.core.initializers;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
        var input = new ElementsInitializationInput(Application.empty(), this.getClass());
        when(elementContext.getName()).thenReturn("testElement");
        var expectedElements = ElementsContainer.fromElementContexts(List.of(elementContext));
        when(elementsInitializationProcess.initialize(input)).thenReturn(expectedElements);

        var actualElements = elementsCoreInitializer.initialize(input);
        assertEquals(expectedElements, actualElements);
    }

}