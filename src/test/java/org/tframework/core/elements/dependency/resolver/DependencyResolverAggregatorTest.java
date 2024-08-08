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
package org.tframework.core.elements.dependency.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.reflection.annotations.PreScannedAnnotations;
import org.tframework.core.utils.LogUtils;

@ExtendWith(MockitoExtension.class)
class DependencyResolverAggregatorTest {

    private static final String DEPENDENCY_DECLARED_AS = "Unit test";

    @Mock
    private ElementContext originalElementContext;

    @Mock
    private BasicDependencyResolver basicDependencyResolver;

    @Mock
    private ElementDependencyResolver elementDependencyResolver;

    @Mock
    private PreScannedAnnotations preScannedAnnotations;

    private List<DependencyResolver> dependencyResolvers;
    private DependencyResolverAggregator aggregator;
    private DependencyDefinition dependencyDefinition;

    @BeforeEach
    void setUp() throws Exception {
        dependencyResolvers = List.of(basicDependencyResolver, elementDependencyResolver);
        aggregator = DependencyResolverAggregator.usingResolvers(dependencyResolvers);

        Field someField = this.getClass().getDeclaredField("someString");
        dependencyDefinition = DependencyDefinition.fromField(someField);
    }

    @Test
    void shouldResolveDependency() {
        String dependencyValue = "someValue";
        when(basicDependencyResolver.resolveDependency(dependencyDefinition, preScannedAnnotations))
                .thenReturn(Optional.of(dependencyValue));

        var dependencyGraph = ElementDependencyGraph.empty();
        Object resolvedDependency = aggregator.resolveDependency(
                dependencyDefinition,
                originalElementContext,
                dependencyGraph,
                preScannedAnnotations,
                DEPENDENCY_DECLARED_AS
        );

        assertEquals(dependencyValue, resolvedDependency);
    }

    @Test
    void shouldThrowDependencyResolutionException_whenDependencyCannotBeResolved() {
        var dependencyGraph = ElementDependencyGraph.empty();

        when(basicDependencyResolver.resolveDependency(dependencyDefinition, preScannedAnnotations))
                .thenReturn(Optional.empty());
        when(elementDependencyResolver.resolveDependency(dependencyDefinition, originalElementContext, dependencyGraph, preScannedAnnotations))
                .thenReturn(Optional.empty());

        var e = assertThrows(DependencyResolutionException.class, () -> aggregator.resolveDependency(
                        dependencyDefinition,
                        originalElementContext,
                        dependencyGraph,
                        preScannedAnnotations,
                        DEPENDENCY_DECLARED_AS
                )
        );

        String expectedMessage = e.getMessageTemplate().formatted(
                DEPENDENCY_DECLARED_AS,
                dependencyDefinition.dependencyType().getName(),
                LogUtils.objectClassNames(dependencyResolvers)
        );
        assertEquals(expectedMessage, e.getMessage());
    }

    private String someString;

}
