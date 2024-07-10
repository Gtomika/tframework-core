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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.handler.SpecialDependencyHandlerAggregator;
import org.tframework.core.elements.dependency.resolver.helper.ElementDependencyResolverHelper;

@ExtendWith(MockitoExtension.class)
class FallbackDependencyResolverTest {

    @Mock
    private ElementsContainer elementsContainer;

    @Mock
    private ElementContext originalElementContext;

    @Mock
    private ElementDependencyResolverHelper resolverHelper;

    @Mock
    private SpecialDependencyHandlerAggregator specialDependencyHandlerAggregator;

    @Mock
    private ElementDependencyGraph dependencyGraph;

    private FallbackDependencyResolver fallbackDependencyResolver;
    private DependencyDefinition dependencyDefinition;

    @BeforeEach
    void setUp() throws Exception {
        fallbackDependencyResolver = new FallbackDependencyResolver(
                elementsContainer,
                resolverHelper,
                specialDependencyHandlerAggregator
        );
        Field someField = this.getClass().getDeclaredField("someString");
        dependencyDefinition = new DependencyDefinition(someField, someField.getType());
    }

    @Test
    public void shouldResolveDependency_whenHandledBySpecialHandler() {
        var specialDependencyValue = List.of(1);

        when(specialDependencyHandlerAggregator.handleDependency(
                elementsContainer, dependencyDefinition, originalElementContext, dependencyGraph
        )).thenReturn(Optional.of(specialDependencyValue));

        var resolvedDependency = fallbackDependencyResolver.resolveDependency(
                dependencyDefinition,
                originalElementContext,
                dependencyGraph
        );

        assertTrue(resolvedDependency.isPresent());
        assertEquals(specialDependencyValue, resolvedDependency.get());
    }

    @Test
    public void shouldResolveDependency_whenPassedToByTypeResolverHelper() {
        String expectedDependencyValue = "testDependencyValue";

        when(resolverHelper.resolveElementDependency(
                elementsContainer, originalElementContext, dependencyDefinition, null, dependencyGraph
        )).thenReturn(expectedDependencyValue);

        var resolvedDependency = fallbackDependencyResolver.resolveDependency(
                dependencyDefinition,
                originalElementContext,
                dependencyGraph
        );

        assertTrue(resolvedDependency.isPresent());
        assertEquals(expectedDependencyValue, resolvedDependency.get());
    }

    @Test
    public void shouldNotResolveDependency_whenExceptionHappensDuringResolution() {
        when(resolverHelper.resolveElementDependency(
                elementsContainer, originalElementContext, dependencyDefinition, null, dependencyGraph
        )).thenThrow(new RuntimeException("Oof, dependency not found"));

        var resolvedDependency = fallbackDependencyResolver.resolveDependency(
                dependencyDefinition,
                originalElementContext,
                ElementDependencyGraph.empty()
        );
        assertTrue(resolvedDependency.isEmpty());
    }

    @InjectElement
    private String someString;
}
