package org.tframework.core.elements.dependency.graph;

import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.tframework.core.elements.context.ElementContext;

import java.util.stream.Collectors;

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
public class ElementDependencyGraph {

    private final DefaultDirectedGraph<ElementContext, DefaultEdge> graph;

    private ElementDependencyGraph() {
        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    }

    /**
     * Adds a dependency to the graph. If the dependency already exists, this will do nothing.
     * This allows to add circular dependencies, as validation will only happen in {@link #detectCircularDependencies()}.
     * @param original The element that depends on the other.
     * @param dependency The element that is depended on.
     */
    public void addDependency(ElementContext original, ElementContext dependency) {
        graph.addVertex(original); //if vertexes or edges already exists, this will do nothing
        graph.addVertex(dependency);
        graph.addEdge(dependency, original);
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
    public void detectCircularDependencies() throws CircularDependencyException {
        var cycleDetector = new CycleDetector<>(graph);
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
