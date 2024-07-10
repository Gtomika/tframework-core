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
package org.tframework.core.elements.dependency.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.dependency.DependencyDefinition;

public class StringMapDependencyHandlerTest extends SpecialDependencyHandlerBaseTest {

    private static final String DEPENDENCY_NAME = "a";

    @BeforeEach
    public void setUp() {
        handler = new StringMapDependencyHandler();
    }

    private final String stringDependency = "";

    @Test
    public void shouldNotHandleNonMapDependency() throws Exception {
        var dependencyDefinition = new DependencyDefinition(
                this.getClass().getDeclaredField("stringDependency"),
                String.class
        );
        var handledResult = whenSpecialDependencyHandlerIsCalled(dependencyDefinition);
        assertTrue(handledResult.isEmpty());
    }

    private final Map<Integer, String> notStringKeyedMapDependency = Map.of();

    @Test
    public void shouldNotHandleNonStringKeyedMapDependency() throws Exception {
        var dependencyDefinition = new DependencyDefinition(
                this.getClass().getDeclaredField("notStringKeyedMapDependency"),
                notStringKeyedMapDependency.getClass()
        );
        var handledResult = whenSpecialDependencyHandlerIsCalled(dependencyDefinition);
        assertTrue(handledResult.isEmpty());
    }

    private final Map<String, Integer> stringMapDependency = Map.of(DEPENDENCY_NAME, 1);

    @Test
    public void shouldHandleStringMapDependency() throws Exception {
        when(elementsContainer.getElementContextsWithType(Integer.class)).thenReturn(List.of(dependencyElementContext));
        when(dependencyElementContext.requestInstance(graph)).thenReturn(stringMapDependency.get(DEPENDENCY_NAME));
        when(dependencyElementContext.getName()).thenReturn(DEPENDENCY_NAME);

        var dependencyDefinition = new DependencyDefinition(
                this.getClass().getDeclaredField("stringMapDependency"),
                stringMapDependency.getClass()
        );
        var handledResult = whenSpecialDependencyHandlerIsCalled(dependencyDefinition);

        assertTrue(handledResult.isPresent());
        assertEquals(stringMapDependency, handledResult.get());
        verify(graph).addDependency(originalElementContext, dependencyElementContext);
    }
}
