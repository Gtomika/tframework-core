package org.tframework.core.elements.dependency.graph;

import lombok.Builder;
import org.tframework.core.TFrameworkException;

import java.util.Set;

/**
 * Thrown when a dependency cycle is detected.
 */
public class CircularDependencyException extends TFrameworkException {

    private static final String TEMPLATE = """
            Circular dependency detected!
            The following subgraph of the dependency graph contains a cycle:
            %s
            Please check the listed elements and ensure that there is no circular dependency between them.
            """;

    @Builder
    public CircularDependencyException(String subgraphStringWithCycle) {
        super(TEMPLATE.formatted(subgraphStringWithCycle));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
