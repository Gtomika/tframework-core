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

import lombok.Builder;
import org.tframework.core.TFrameworkException;

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
