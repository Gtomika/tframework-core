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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.dependency.DependencyDefinition;

public class ListDependencyHandlerTest extends SpecialDependencyHandlerBaseTest {

    @BeforeEach
    public void setUp() {
        handler = new ListDependencyHandler();
    }

    private final String stringDependency = "";

    @Test
    public void shouldNotHandleNonListDependency() throws Exception {
        var dependencyDefinition = new DependencyDefinition(
                this.getClass().getDeclaredField("stringDependency"),
                String.class
        );
        var handledResult = whenSpecialDependencyHandlerIsCalled(dependencyDefinition);
        assertTrue(handledResult.isEmpty());
    }

    private final List<Integer> listDependency = List.of(1);

    @Test
    public void shouldHandleListDependency() throws Exception {
        when(elementsContainer.getElementContextsWithType(Integer.class)).thenReturn(List.of(dependencyElementContext));
        when(dependencyElementContext.requestInstance(graph)).thenReturn(listDependency.getFirst());

        var dependencyDefinition = new DependencyDefinition(
                this.getClass().getDeclaredField("listDependency"),
                listDependency.getClass()
        );
        var handledResult = whenSpecialDependencyHandlerIsCalled(dependencyDefinition);

        assertTrue(handledResult.isPresent());
        assertEquals(listDependency, handledResult.get());
        verify(graph).addDependency(originalElementContext, dependencyElementContext);
    }
}
