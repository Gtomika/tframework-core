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

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

@ExtendWith(MockitoExtension.class)
public class ResolverHelperBaseTest {

    protected static final String DEPENDENCY_NAME = "dependencyName";

    @Mock
    protected ElementsContainer elementsContainer;

    @Mock
    protected ElementContext originalElementContext;

    @Mock
    protected ElementContext dependencyElementContext;

    @Mock
    protected DependencyDefinition dependencyDefinition;

    @Mock
    protected ElementDependencyGraph dependencyGraph;

    protected ElementDependencyResolverHelper helper;
}
