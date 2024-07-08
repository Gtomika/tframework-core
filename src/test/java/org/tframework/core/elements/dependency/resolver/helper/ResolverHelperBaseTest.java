/* Licensed under Apache-2.0 2024. */
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
