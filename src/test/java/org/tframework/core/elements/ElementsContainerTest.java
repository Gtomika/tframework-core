/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.context.ElementContext;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ElementsContainerTest {

    private static final String ELEMENT_NAME = "test";

    @Mock
    private ElementContext elementContext;

    public ElementsContainerTest() {
    }

    @Test
    public void shouldGetElementByName() {
        when(elementContext.getName()).thenReturn(ELEMENT_NAME);
        var elementsContainer = ElementsContainer.fromElementContexts(List.of(elementContext));
        assertEquals(elementContext, elementsContainer.getElementContext(ELEMENT_NAME));
    }

    @Test
    public void shouldThrowException_whenElementByNameDoesNotExist() {
        var elementsContainer = ElementsContainer.empty();
        var exception = assertThrows(ElementNotFoundException.class, () -> {
            elementsContainer.getElementContext(ELEMENT_NAME);
        });
        assertEquals(
                exception.getMessageTemplate().formatted(ELEMENT_NAME),
                exception.getMessage()
        );
    }

    @Test
    public void shouldGetElementByType() {
        //not specified name will default to the class name
        when(elementContext.getName()).thenReturn(ElementUtils.getElementNameByType(Integer.class));
        var elementsContainer = ElementsContainer.fromElementContexts(List.of(elementContext));
        assertEquals(elementContext, elementsContainer.getElementContext(Integer.class));
    }

    @Test
    public void shouldThrowException_whenElementByTypeDoesNotExists() {
        var elementsContainer = ElementsContainer.empty();
        var exception = assertThrows(ElementNotFoundException.class, () -> {
            elementsContainer.getElementContext(File.class);
        });
        assertEquals(
                exception.getMessageTemplate().formatted(ElementUtils.getElementNameByType(File.class)),
                exception.getMessage()
        );
    }

    @Test
    public void shouldAddElement_whenElementNameIsUnique() {
        when(elementContext.getName()).thenReturn(ELEMENT_NAME);
        var elementsContainer = ElementsContainer.empty();
        elementsContainer.addElementContext(elementContext);
        assertEquals(elementContext, elementsContainer.getElementContext(ELEMENT_NAME));
    }

    @Test
    public void shouldThrowException_whenElementNameIsNotUnique() {
        when(elementContext.getName()).thenReturn(ELEMENT_NAME);
        var elementsContainer = ElementsContainer.fromElementContexts(List.of(elementContext));

        var exception = assertThrows(ElementNameNotUniqueException.class, () -> {
            elementsContainer.addElementContext(elementContext);
        });
        assertEquals(
                exception.getMessageTemplate().formatted(ELEMENT_NAME),
                exception.getMessage()
        );
    }

}
