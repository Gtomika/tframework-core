/*
Copyright 2024 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
