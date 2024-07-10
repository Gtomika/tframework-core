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
package org.tframework.core.elements.dependency.resolver.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ElementByTypeResolverHelperTest extends ResolverHelperBaseTest {

    @BeforeEach
    public void setUp() {
        helper = new ElementByTypeResolverHelper();
    }

    @Test
    public void shouldResolveByType() {
        Object expectedDependency = new Object();
        doReturn(Object.class).when(dependencyDefinition).dependencyType();
        when(elementsContainer.getElementContext(Object.class)).thenReturn(dependencyElementContext);
        when(dependencyElementContext.requestInstance(dependencyGraph)).thenReturn(expectedDependency);

        Object actualDependency = helper.resolveElementDependency(
                elementsContainer,
                originalElementContext,
                dependencyDefinition,
                null,
                dependencyGraph
        );

        assertEquals(expectedDependency, actualDependency);
        verify(dependencyGraph).addDependency(originalElementContext, dependencyElementContext);
    }

}
