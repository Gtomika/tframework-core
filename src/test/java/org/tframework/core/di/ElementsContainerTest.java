/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.di.annotations.Element;
import org.tframework.core.di.context.PrototypeElementContext;
import org.tframework.core.di.context.SingletonElementContext;
import org.tframework.core.di.context.source.ClassElementSource;
import org.tframework.core.di.context.source.ElementSource;

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
        var elementsContainer = ElementsContainer.fromElementContexts(List.of());
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
        var elementsContainer = ElementsContainer.fromElementContexts(List.of());
        var exception = assertThrows(ElementNotFoundException.class, () -> {
            elementsContainer.getElementContext(File.class);
        });
        assertEquals(
                exception.getMessageTemplate().formatted(ElementUtils.getElementNameByType(File.class)),
                exception.getMessage()
        );
    }

}
