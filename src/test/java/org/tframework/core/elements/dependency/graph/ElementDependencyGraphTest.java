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
package org.tframework.core.elements.dependency.graph;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.tframework.core.elements.context.ElementContext;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ElementDependencyGraphTest {

    @Mock
    private ElementContext elementA;

    @Mock
    private ElementContext elementB;

    @Mock
    private ElementContext elementC;

    @BeforeEach
    void setUp() {
        when(elementA.getName()).thenReturn("elementA");
        when(elementB.getName()).thenReturn("elementB");
        when(elementC.getName()).thenReturn("elementC");
    }

    @Test
    public void shouldNotDetectCycles_whenDependenciesAreValid() {
        var dependencyGraph = ElementDependencyGraph.empty();
        assertDoesNotThrow(() ->  dependencyGraph.addDependency(elementA, elementB));
    }

    @Test
    public void shouldThrowException_whenCyclicDependency() {
        var dependencyGraph = ElementDependencyGraph.empty();
        dependencyGraph.addDependency(elementA, elementB);
        dependencyGraph.addDependency(elementB, elementC);

        var exception = assertThrows(CircularDependencyException.class, () -> {
            dependencyGraph.addDependency(elementC, elementA); //adding this would create a cycle
        });

        var cycleRepresentation = List.of("elementA", "elementB", "elementC").toString();
        String expectedMessage = exception.getMessageTemplate().formatted(cycleRepresentation);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowException_whenElementDependsOnItself() {
        var dependencyGraph = ElementDependencyGraph.empty();

        var exception = assertThrows(CircularDependencyException.class, () -> {
            dependencyGraph.addDependency(elementA, elementA);
        });

        var cycleRepresentation = List.of("elementA").toString();
        String expectedMessage = exception.getMessageTemplate().formatted(cycleRepresentation);
        assertEquals(expectedMessage, exception.getMessage());
    }

}
