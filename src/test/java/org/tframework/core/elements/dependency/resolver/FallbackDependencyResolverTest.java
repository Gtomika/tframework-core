/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

@ExtendWith(MockitoExtension.class)
class FallbackDependencyResolverTest {

    @Mock
    private ElementsContainer dependencySource;

    @Mock
    private ElementContext originalElementContext;

    @Mock
    private ElementContext dependencyElementContext;

    private FallbackDependencyResolver fallbackDependencyResolver;
    private Field someField;
    private DependencyDefinition dependencyDefinition;

    @BeforeEach
    void setUp() throws Exception {
        fallbackDependencyResolver = new FallbackDependencyResolver(dependencySource);
        someField = this.getClass().getDeclaredField("someString");
        dependencyDefinition = new DependencyDefinition(someField, someField.getType());
    }

    @Test
    public void shouldResolveDependency_whenPresentInDependencySource() {
        var dependencyGraph = ElementDependencyGraph.empty();
        String expectedDependencyValue = "testDependencyValue";

        when(dependencySource.getElementContext(someField.getType()))
                .thenReturn(dependencyElementContext);
        when(dependencyElementContext.requestInstance(dependencyGraph))
                .thenReturn(expectedDependencyValue);

        var resolvedDependency = fallbackDependencyResolver.resolveDependency(
                dependencyDefinition,
                originalElementContext,
                dependencyGraph
        );

        assertTrue(dependencyGraph.containsDependency(originalElementContext, dependencyElementContext));
        if(resolvedDependency.isPresent() && resolvedDependency.get() instanceof String resolvedString) {
            assertEquals(expectedDependencyValue, resolvedString);
        } else {
            fail("Resolved dependency is not a String");
        }
    }

    @Test
    public void shouldNotResolveDependency_whenNotPresentInDependencySource() {
        when(dependencySource.getElementContext(someField.getType()))
                .thenThrow(new RuntimeException("Oof, dependency not found"));

        var resolvedDependency = fallbackDependencyResolver.resolveDependency(
                dependencyDefinition,
                originalElementContext,
                ElementDependencyGraph.empty()
        );
        assertTrue(resolvedDependency.isEmpty());
    }

    private String someString;

}
