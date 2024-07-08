/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ElementByNameResolverHelperTest extends ResolverHelperBaseTest {

    @BeforeEach
    public void setUp() {
        helper = new ElementByNameResolverHelper();
    }

    @Test
    public void shouldResolveByName() {
        Object expectedDependency = new Object();
        when(elementsContainer.getElementContext(DEPENDENCY_NAME)).thenReturn(dependencyElementContext);
        when(dependencyElementContext.requestInstance(dependencyGraph)).thenReturn(expectedDependency);

        Object actualDependency = helper.resolveElementDependency(
                elementsContainer,
                originalElementContext,
                dependencyDefinition,
                DEPENDENCY_NAME,
                dependencyGraph
        );

        assertEquals(expectedDependency, actualDependency);
        verify(dependencyGraph).addDependency(originalElementContext, dependencyElementContext);
    }
}
