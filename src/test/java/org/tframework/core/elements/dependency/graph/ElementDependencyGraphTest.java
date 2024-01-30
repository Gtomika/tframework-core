package org.tframework.core.elements.dependency.graph;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.tframework.core.elements.context.ElementContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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