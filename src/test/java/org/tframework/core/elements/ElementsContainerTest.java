/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.PrototypeElementContext;
import org.tframework.core.elements.context.SingletonElementContext;
import org.tframework.core.elements.context.source.ClassElementSource;
import org.tframework.core.elements.context.source.ElementSource;

class ElementsContainerTest {

    public ElementsContainerTest() {
    }

    private static final String ELEMENT_NAME = "test";
    private static final ElementSource TEST_SOURCE = new ClassElementSource(ElementsContainerTest.class.getConstructors()[0]);

    @Test
    public void shouldGetElementByName() {
        var elementContext = new SingletonElementContext(ELEMENT_NAME, String.class, TEST_SOURCE);
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
        var elementContext = new PrototypeElementContext(Element.NAME_NOT_SPECIFIED, Integer.class, TEST_SOURCE);
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
        var elementsContainer = ElementsContainer.empty();
        var elementContext = new SingletonElementContext(ELEMENT_NAME, String.class, TEST_SOURCE);
        elementsContainer.addElementContext(elementContext);
        assertEquals(elementContext, elementsContainer.getElementContext(ELEMENT_NAME));
    }

    @Test
    public void shouldThrowException_whenElementNameIsNotUnique() {
        var elementContext = new SingletonElementContext(ELEMENT_NAME, String.class, TEST_SOURCE);
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
