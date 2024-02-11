/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.tframework.core.elements.context.ElementContext;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ElementsContainerTest {

    private static final String ELEMENT_NAME = "test";

    @Mock
    private ElementContext elementContext;

    @Mock
    private ElementByTypeResolver elementByTypeResolver;

    @BeforeEach
    void setUp() {
        when(elementContext.getName()).thenReturn(ELEMENT_NAME);
    }

    @Test
    public void shouldGetElementByName() {
        var elementsContainer = new ElementsContainer(List.of(elementContext), elementByTypeResolver);

        assertEquals(elementContext, elementsContainer.getElementContext(ELEMENT_NAME));
    }

    @Test
    public void shouldThrowException_whenElementByNameDoesNotExist() {
        var elementsContainer = new ElementsContainer(List.of(), elementByTypeResolver);

        var exception = assertThrows(ElementNotFoundException.class, () -> {
            elementsContainer.getElementContext(ELEMENT_NAME);
        });

        assertEquals(
                exception.getMessageTemplate().formatted(ElementNotFoundException.HAS_NAME, ELEMENT_NAME),
                exception.getMessage()
        );
    }

    //since getElementContext(Class) delegates to the resolver, detailed test coverage is provided there
    @Test
    public void shouldGetElementByType() {
        var elements = List.of(elementContext);
        when(elementContext.getName()).thenReturn(ELEMENT_NAME);
        when(elementByTypeResolver.getElementByType(elements, Integer.class)).thenReturn(elementContext);

        var elementsContainer = new ElementsContainer(elements, elementByTypeResolver);

        assertEquals(elementContext, elementsContainer.getElementContext(Integer.class));
    }

    //since hasElementContext(Class) delegates to the resolver, detailed test coverage is provided there
    @Test
    public void shouldCheckIfHasElementByType() {
        var elements = List.of(elementContext);
        when(elementByTypeResolver.hasElementByType(elements, Integer.class)).thenReturn(true);

        var elementsContainer = new ElementsContainer(elements, elementByTypeResolver);

        assertTrue(elementsContainer.hasElementContext(Integer.class));
    }

    @Test
    public void shouldAddElement_whenElementNameIsUnique() {
        var elementsContainer = new ElementsContainer(List.of(), elementByTypeResolver);

        elementsContainer.addElementContext(elementContext);

        assertEquals(elementContext, elementsContainer.getElementContext(ELEMENT_NAME));
    }

    @Test
    public void shouldThrowException_whenElementNameIsNotUnique() {
        var elementsContainer = new ElementsContainer(List.of(elementContext), elementByTypeResolver);

        var exception = assertThrows(ElementNameNotUniqueException.class, () -> {
            elementsContainer.addElementContext(elementContext);
        });
        assertEquals(
                exception.getMessageTemplate().formatted(ELEMENT_NAME, elementContext, elementContext),
                exception.getMessage()
        );
    }

    @Test
    public void shouldOverrideElement() {
        var elementsContainer = new ElementsContainer(List.of(elementContext), elementByTypeResolver);

        //overriding it with itself
        boolean overrideHappened = elementsContainer.overrideElementContext(elementContext);
        assertTrue(overrideHappened);
    }

}
