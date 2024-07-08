/* Licensed under Apache-2.0 2024. */
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
