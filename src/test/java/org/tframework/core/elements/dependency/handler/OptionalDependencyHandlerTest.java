/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.dependency.DependencyDefinition;

public class OptionalDependencyHandlerTest extends SpecialDependencyHandlerBaseTest {

    @BeforeEach
    public void setUp() {
        handler = new OptionalDependencyHandler();
    }

    private final String stringDependency = "";

    @Test
    public void shouldNotHandleNonOptionalDependency() throws Exception {
        var dependencyDefinition = new DependencyDefinition(
                this.getClass().getDeclaredField("stringDependency"),
                String.class
        );
        var handledResult = whenSpecialDependencyHandlerIsCalled(dependencyDefinition);
        assertTrue(handledResult.isEmpty());
    }

    private final Optional<Integer> optionalDependency = Optional.of(1);

    @Test
    public void shouldHandleOptionalDependency() throws Exception {
        when(elementsContainer.getElementContext(Integer.class)).thenReturn(dependencyElementContext);
        when(dependencyElementContext.requestInstance(graph)).thenReturn(optionalDependency.get());

        var dependencyDefinition = new DependencyDefinition(
                this.getClass().getDeclaredField("optionalDependency"),
                optionalDependency.getClass()
        );
        var handledResult = whenSpecialDependencyHandlerIsCalled(dependencyDefinition);

        assertTrue(handledResult.isPresent());
        assertEquals(optionalDependency, handledResult.get());
    }
}
