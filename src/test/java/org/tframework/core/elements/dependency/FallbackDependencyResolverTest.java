/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency;

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
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.ElementsContainer;

@ExtendWith(MockitoExtension.class)
class FallbackDependencyResolverTest {

    @Mock
    private ElementsContainer dependencySource;

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
        String resolvedDependencyValue = "testDependencyValue";
        when(dependencySource.requestDependency(ElementUtils.getElementNameByType(someField.getType())))
                .thenReturn(resolvedDependencyValue);

        var resolvedDependency = fallbackDependencyResolver.resolveDependency(dependencyDefinition);
        if(resolvedDependency.isPresent() && resolvedDependency.get() instanceof String resolvedString) {
            assertEquals(resolvedDependencyValue, resolvedString);
        } else {
            fail("Resolved dependency is not a String");
        }
    }

    @Test
    public void shouldNotResolveDependency_whenNotPresentInDependencySource() {
        when(dependencySource.requestDependency(ElementUtils.getElementNameByType(someField.getType())))
                .thenThrow(new RuntimeException("Oof, dependency not found"));

        var resolvedDependency = fallbackDependencyResolver.resolveDependency(dependencyDefinition);
        assertTrue(resolvedDependency.isEmpty());
    }

    private String someString;

}
