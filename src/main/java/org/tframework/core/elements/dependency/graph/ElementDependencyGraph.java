/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.graph;

import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.tframework.core.elements.context.ElementContext;

/**
 * The dependency graph wraps a {@link DirectedAcyclicGraph} and provides a few convenience methods.
 * If element A depends on element B, then there will be an edge from B to A in the graph.
 * Its job is to make sure that there are no cycles among the dependencies, and break the dependency
 * resolution process if there are. An element depending on itself is treated as a cycle. For example, this is not allowed:
 *
 * <pre>{@code
 * @Element
 * public class ElementA {
 *
 *    public ElementA(ElementA elementA) {}
 *
 * }
 * }</pre>
 *
 * Please note that a method element, additionally to the other dependencies, also depends on its parent element. For
 * example, this would create a cycle:
 *
 * <pre>{@code
 * @Element
 * public class ParentElement {
 *
 *   public ParentElement(MethodElement methodElement) {}
 *
 *   @Element
 *   public MethodElement methodElement() {
 *      return new MethodElement();
 *   }
 *
 * }
 * }</pre>
 *
 * In case of cyclic dependencies, the framework will throw a {@link CircularDependencyException}.
 */
@EqualsAndHashCode
public class ElementDependencyGraph {

    private final DirectedAcyclicGraph<ElementContext, DefaultEdge> graph;

    private ElementDependencyGraph() {
        this.graph = new DirectedAcyclicGraph<>(DefaultEdge.class);
    }

    /**
     * Adds a dependency to the graph. If the dependency already exists, this will do nothing.
     * Circular dependencies will raise an exception.
     * @param original The element that depends on the other.
     * @param dependency The element that is depended on.
     * @throws CircularDependencyException If a cycle is found. The exception message will have the details about
     * the cycle.
     */
    public void addDependency(ElementContext original, ElementContext dependency) throws CircularDependencyException {
        graph.addVertex(original); //if vertexes or edges already exists, this will do nothing
        graph.addVertex(dependency);
        try {
            graph.addEdge(dependency, original);
        } catch(IllegalArgumentException e) {
            //create a copy of the graph which has the circle
            var copyAllowsCycles = GraphUtils.copyDependencyGraphAndAllowCycles(graph);
            copyAllowsCycles.addEdge(dependency, original);
            detectCircularDependencies(copyAllowsCycles);
        }
    }

    /**
     * Checks if the dependency graph contains the {@code dependency -> original} edge.
     */
    public boolean containsDependency(ElementContext original, ElementContext dependency) {
        return graph.containsEdge(dependency, original);
    }

    /**
     * Finds cycles in the dependency graph.
     * @throws CircularDependencyException If a cycle is found. The exception message will have the details about
     * the cycle.
     */
    public void detectCircularDependencies(DefaultDirectedGraph<ElementContext, DefaultEdge> graphToCheck) throws CircularDependencyException {
        var cycleDetector = new CycleDetector<>(graphToCheck);
        var cycle = cycleDetector.findCycles();
        if(!cycle.isEmpty()) {
            var cycleElementNames = cycle.stream()
                    .map(ElementContext::getName)
                    .collect(Collectors.toSet())
                    .toString();
            throw new CircularDependencyException(cycleElementNames);
        }
    }

    @Override
    public String toString() {
        return graph.toString();
    }

    /**
     * Constructs a dependency graph that contains no dependencies.
     */
    public static ElementDependencyGraph empty() {
        return new ElementDependencyGraph();
    }

}
