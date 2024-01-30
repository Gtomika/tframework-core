package org.tframework.core.elements.dependency.graph;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.tframework.core.elements.context.ElementContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GraphUtils {

    /**
     * Makes a copy of the acyclic dependency graph to a structure that allows cycles, but
     * otherwise have the same edges and vertexes.
     * @param dependencyGraph The graph to copy.
     * @return A {@link DefaultDirectedGraph} which is the same as the dependency graph but allows cycles.
     */
    public static DefaultDirectedGraph<ElementContext, DefaultEdge> copyDependencyGraphAndAllowCycles(
            @NonNull DirectedAcyclicGraph<ElementContext, DefaultEdge> dependencyGraph
    ) {
        var dependencyGraphCyclesAllowed = new DefaultDirectedGraph<ElementContext, DefaultEdge>(DefaultEdge.class);

        dependencyGraph.vertexSet().forEach(dependencyGraphCyclesAllowed::addVertex);
        dependencyGraph.edgeSet().forEach(edge -> {
            var source = dependencyGraphCyclesAllowed.getEdgeSource(edge);
            var target = dependencyGraphCyclesAllowed.getEdgeTarget(edge);
            dependencyGraphCyclesAllowed.addEdge(source, target);
        });

        return dependencyGraphCyclesAllowed;
    }

}
