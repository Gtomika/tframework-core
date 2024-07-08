/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

@ExtendWith(MockitoExtension.class)
public class SpecialDependencyHandlerAggregatorTest {

    private static final String SPECIAL_VALUE = "value";

    //only used with mocks, does not matter what is in it
    private static final DependencyDefinition DEPENDENCY_DEFINITION = new DependencyDefinition(
            SpecialDependencyHandlerAggregatorTest.class, String.class
    );

    @Mock
    protected ElementsContainer elementsContainer;

    @Mock
    protected ElementContext originalElementContext;

    @Mock
    protected ElementDependencyGraph graph;

    @Mock
    private SpecialElementDependencyHandler handler;

    private SpecialDependencyHandlerAggregator handlerAggregator;

    @BeforeEach
    public void setUp() {
        handlerAggregator = SpecialDependencyHandlerAggregator.usingHandlers(List.of(handler));
    }

    @Test
    public void shouldHandleDependencyWhenContainedHandlerHandlesIt() {
        when(handler.handleDependency(elementsContainer, DEPENDENCY_DEFINITION, originalElementContext, graph))
                .thenReturn(Optional.of(SPECIAL_VALUE));
        var handledResult = handlerAggregator.handleDependency(elementsContainer, DEPENDENCY_DEFINITION, originalElementContext, graph);

        assertTrue(handledResult.isPresent());
        assertEquals(SPECIAL_VALUE, handledResult.get());
    }

    @Test
    public void shouldNotHandleDependencyWhenNoContainedHandlerHandlesIt() {
        when(handler.handleDependency(elementsContainer, DEPENDENCY_DEFINITION, originalElementContext, graph))
                .thenReturn(Optional.empty());
        var handledResult = handlerAggregator.handleDependency(elementsContainer, DEPENDENCY_DEFINITION, originalElementContext, graph);

        assertTrue(handledResult.isEmpty());
    }
}
