/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

import java.io.File;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.tframework.core.elements.context.ElementContext;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ElementByTypeResolverTest {

    @Mock
    private ElementContext t1Context;

    @Mock
    private ElementContext t2Context;

    private final ElementByTypeResolver resolver = new ElementByTypeResolver();

    @BeforeEach
    void setUp() {
        doReturn(T1.class).when(t1Context).getType();
        doReturn("t1").when(t1Context).getName();

        doReturn(T2.class).when(t2Context).getType();
        doReturn("t2").when(t2Context).getName();
    }

    @Test
    public void shouldGetElementWithExactType() {
        var contexts = List.of(t1Context, t2Context);
        var selectedContext = resolver.getElementByType(contexts, T1.class);
        assertEquals(t1Context, selectedContext);
    }

    @Test
    public void shouldThrowAmbiguousTypeException_whenMultipleElementsWithExactTypeExist() {
        var contexts = List.of(t2Context, t2Context, t1Context);
        var exception = assertThrows(AmbiguousElementTypeException.class, () -> {
            resolver.getElementByType(contexts, T2.class);
        });
        assertTrue(exception.getMessage().contains(T2.class.getName()));
    }

    @Test
    public void shouldGetElementWithAssignableType() {
        var contexts = List.of(t1Context, t2Context);
        var selectedContext = resolver.getElementByType(contexts, I1.class);
        //T1 implements I1, so it should be selected
        assertEquals(t1Context, selectedContext);
    }

    @Test
    public void shouldThrowAmbiguousTypeException_whenMultipleElementsWithAssignableTypeExist() {
        var contexts = List.of(t1Context, t2Context);
        var exception = assertThrows(AmbiguousElementTypeException.class, () -> {
            resolver.getElementByType(contexts, Object.class);
        });
        assertTrue(exception.getMessage().contains(Object.class.getName()));
    }

    @Test
    public void shouldThrowElementNotFoundException_whenNoElementsWithAssignableTypeExist() {
        var contexts = List.of(t1Context, t2Context);
        var exception = assertThrows(ElementNotFoundException.class, () -> {
            resolver.getElementByType(contexts, String.class);
        });
        assertEquals(
                exception.getMessageTemplate().formatted(ElementNotFoundException.ASSIGNABLE_TO_TYPE, String.class.getName()),
                exception.getMessage()
        );
    }

    @ParameterizedTest
    @ValueSource(classes = {T1.class, T2.class, I1.class})
    public void shouldReturnTrue_ifElementsExistWithAssignableType(Class<?> clazz) {
        var contexts = List.of(t1Context, t2Context, t2Context);
        assertTrue(resolver.hasElementByType(contexts, clazz));
    }

    @ParameterizedTest
    @ValueSource(classes = {File.class, String.class})
    public void shouldReturnFalse_ifElementsDoNotExistWithAssignableType(Class<?> clazz) {
        var contexts = List.of(t1Context, t2Context);
        assertFalse(resolver.hasElementByType(contexts, clazz));
    }

    static class T1 implements I1 {}

    static class T2 {}

    interface I1 {}

}
