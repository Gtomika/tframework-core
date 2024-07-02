/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.handler;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.dependency.DependencyDefinition;

public class ArrayDependencyHandlerTest extends SpecialDependencyHandlerBaseTest {

    @BeforeEach
    public void setUp() {
        handler = new ArrayDependencyHandler();
    }

    private final String stringDependency = "";

    @Test
    public void shouldNotHandleNonArrayDependency() throws Exception {
        var dependencyDefinition = new DependencyDefinition(
                this.getClass().getDeclaredField("stringDependency"),
                String.class
        );
        var handledResult = whenSpecialDependencyHandlerIsCalled(dependencyDefinition);
        assertTrue(handledResult.isEmpty());
    }

    private final Integer[] arrayDependency = { 1 };

    @Test
    public void shouldHandleArrayDependency() throws Exception {
        when(elementsContainer.getElementContextsWithType(Integer.class)).thenReturn(List.of(dependencyElementContext));
        when(dependencyElementContext.requestInstance(graph)).thenReturn(arrayDependency[0]);

        var dependencyDefinition = new DependencyDefinition(
                this.getClass().getDeclaredField("arrayDependency"),
                arrayDependency.getClass()
        );
        var handledResult = whenSpecialDependencyHandlerIsCalled(dependencyDefinition);

        assertTrue(handledResult.isPresent());
        assertArrayEquals(arrayDependency, (Integer[])handledResult.get());
    }
}
