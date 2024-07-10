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

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.properties.PropertiesContainer;

@ExtendWith(MockitoExtension.class)
class DependencyResolversFactoryTest {

    @Mock
    private ElementsContainer elementsContainer;

    @Mock
    private PropertiesContainer propertiesContainer;

    private final DependencyResolutionInput input = new DependencyResolutionInput(elementsContainer, propertiesContainer);

    @Test
    void shouldCreateParameterDependencyResolvers() {
        var resolvers = DependencyResolversFactory.createParameterDependencyResolvers(input);
        assertInstanceOf(AnnotatedElementDependencyResolver.class, resolvers.get(0));
        assertInstanceOf(PropertyDependencyResolver.class, resolvers.get(1));
        assertInstanceOf(FallbackDependencyResolver.class, resolvers.get(2));
    }

    @Test
    void shouldCreateFieldDependencyResolvers() {
        var resolvers = DependencyResolversFactory.createFieldDependencyResolvers(input);
        assertInstanceOf(AnnotatedElementDependencyResolver.class, resolvers.get(0));
        assertInstanceOf(PropertyDependencyResolver.class, resolvers.get(1));
        assertTrue(resolvers.stream().noneMatch(r -> r instanceof FallbackDependencyResolver));
    }
}
