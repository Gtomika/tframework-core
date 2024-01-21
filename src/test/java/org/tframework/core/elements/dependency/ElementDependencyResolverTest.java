/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.annotations.InjectElement;

@ExtendWith(MockitoExtension.class)
class ElementDependencyResolverTest {

    @Mock
    private InjectAnnotationScanner injectAnnotationScanner;

    @Mock
    private DependencySource dependencySource;

    private ElementDependencyResolver elementDependencyResolver;

    private Field someField;
    private InjectElement injectElementWithNameProvided;
    private DependencyDefinition dependencyDefinitionWithNameProvided;

    private Field otherField;
    private InjectElement injectElementWithNameNotProvided;
    private DependencyDefinition dependencyDefinitionWithNameNotProvided;

    @BeforeEach
    void setUp() throws Exception {
        elementDependencyResolver = new ElementDependencyResolver(injectAnnotationScanner);

        someField = this.getClass().getDeclaredField("someString");
        injectElementWithNameProvided = someField.getAnnotation(InjectElement.class);
        dependencyDefinitionWithNameProvided = new DependencyDefinition(someField, someField.getType());

        otherField = this.getClass().getDeclaredField("otherString");
        injectElementWithNameNotProvided = otherField.getAnnotation(InjectElement.class);
        dependencyDefinitionWithNameNotProvided = new DependencyDefinition(otherField, otherField.getType());
    }

    @Test
    public void shouldResolveDependency_whenPresentInDependencySource_withNameProvided() {
        String dependencyValue = "testDependencyValue";
        when(injectAnnotationScanner.findInjectAnnotation(someField, InjectElement.class))
                .thenReturn(Optional.of(injectElementWithNameProvided));
        when(dependencySource.requestDependency(injectElementWithNameProvided.value()))
                .thenReturn(dependencyValue);

        var resolvedDependency = elementDependencyResolver.resolveDependency(dependencySource, dependencyDefinitionWithNameProvided);
        if(resolvedDependency.isPresent() && resolvedDependency.get() instanceof String resolvedString) {
            assertEquals(dependencyValue, resolvedString);
        } else {
            fail("Resolved dependency is not a String");
        }
    }

    @Test
    public void shouldResolveDependency_whenPresentInDependencySource_withNameNotProvided() {
        String dependencyValue = "testDependencyValue";
        when(injectAnnotationScanner.findInjectAnnotation(otherField, InjectElement.class))
                .thenReturn(Optional.of(injectElementWithNameNotProvided));
        when(dependencySource.requestDependency(ElementUtils.getElementNameByType(otherField.getType())))
                .thenReturn(dependencyValue);

        var resolvedDependency = elementDependencyResolver.resolveDependency(dependencySource, dependencyDefinitionWithNameNotProvided);
        if(resolvedDependency.isPresent() && resolvedDependency.get() instanceof String resolvedString) {
            assertEquals(dependencyValue, resolvedString);
        } else {
            fail("Resolved dependency is not a String");
        }
    }

    @Test
    public void shouldNotResolveDependency_whenNotPresentInDependencySource() {
        when(injectAnnotationScanner.findInjectAnnotation(someField, InjectElement.class))
                .thenReturn(Optional.of(injectElementWithNameProvided));
        when(dependencySource.requestDependency(injectElementWithNameProvided.value()))
                .thenThrow(new RuntimeException("Dependency not found"));

        var resolvedDependency = elementDependencyResolver.resolveDependency(dependencySource, dependencyDefinitionWithNameProvided);
        assertTrue(resolvedDependency.isEmpty());
    }

    @Test
    public void shouldThrowException_whenDependencyResolutionFails() {
        when(injectAnnotationScanner.findInjectAnnotation(someField, InjectElement.class))
                .thenThrow(new RuntimeException("Illegal, multiple inject annotations found"));

        assertThrows(RuntimeException.class, () -> {
            elementDependencyResolver.resolveDependency(dependencySource, dependencyDefinitionWithNameProvided);
        });
    }

    @InjectElement("testDependency")
    private String someString;

    @InjectElement // no name provided
    private String otherString;
}
