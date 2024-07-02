/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.dependency.DependencyDefinition;

public class ListDependencyHandlerTest extends SpecialDependencyHandlerBaseTest {

    @BeforeEach
    public void setUp() {
        handler = new ListDependencyHandler();
    }

    private final String stringDependency = "";

    @Test
    public void shouldNotHandleNonListDependency() throws Exception {
        var dependencyDefinition = new DependencyDefinition(
                this.getClass().getDeclaredField("stringDependency"),
                String.class
        );
        var handledResult = whenSpecialDependencyHandlerIsCalled(dependencyDefinition);
        assertTrue(handledResult.isEmpty());
    }

    private final List<Integer> listDependency = List.of(1);

    @Test
    public void shouldHandleListDependency() throws Exception {
        when(elementsContainer.getElementContextsWithType(Integer.class)).thenReturn(List.of(dependencyElementContext));
        when(dependencyElementContext.requestInstance(graph)).thenReturn(listDependency.getFirst());

        var dependencyDefinition = new DependencyDefinition(
                this.getClass().getDeclaredField("listDependency"),
                listDependency.getClass()
        );
        var handledResult = whenSpecialDependencyHandlerIsCalled(dependencyDefinition);

        assertTrue(handledResult.isPresent());
        assertEquals(listDependency, handledResult.get());
    }
}
