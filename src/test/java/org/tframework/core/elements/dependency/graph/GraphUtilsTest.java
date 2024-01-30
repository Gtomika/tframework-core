package org.tframework.core.elements.dependency.graph;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.context.ElementContext;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class GraphUtilsTest {

    @Mock
    private ElementContext v1;

    @Mock
    private ElementContext v2;

    @Test
    void shouldCopyDependencyGraphAndAllowCycles() {
        var dependencyGraph = new DirectedAcyclicGraph<ElementContext, DefaultEdge>(DefaultEdge.class);
        dependencyGraph.addVertex(v1);
        dependencyGraph.addVertex(v2);
        dependencyGraph.addEdge(v1, v2);

        var copyAllowsCycles = GraphUtils.copyDependencyGraphAndAllowCycles(dependencyGraph);

        assertTrue(copyAllowsCycles.containsVertex(v1));
        assertTrue(copyAllowsCycles.containsVertex(v2));
        assertTrue(copyAllowsCycles.containsEdge(v1, v2));

        assertDoesNotThrow(() -> copyAllowsCycles.addEdge(v2, v1)); //this creates a cycle
    }
}