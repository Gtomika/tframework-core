/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.InjectAnnotationScanner;
import org.tframework.core.properties.PropertiesContainer;

@ExtendWith(MockitoExtension.class)
class PropertyDependencyResolverTest {

    @Mock
    private PropertiesContainer propertiesContainer;

    @Mock
    private InjectAnnotationScanner injectAnnotationScanner;

    private PropertyDependencyResolver propertyDependencyResolver;

    private Field someField;
    private InjectProperty injectPropertyAnnotation;
    private DependencyDefinition dependencyDefinition;

    @BeforeEach
    void setUp() throws Exception {
        propertyDependencyResolver = new PropertyDependencyResolver(propertiesContainer, injectAnnotationScanner);

        someField = this.getClass().getDeclaredField("someString");
        injectPropertyAnnotation = someField.getAnnotation(InjectProperty.class);
        dependencyDefinition = new DependencyDefinition(someField, someField.getType());
    }

    @Test
    public void shouldResolveDependency_whenPresentInProperties() {
        String dependencyValue = "test";
        when(injectAnnotationScanner.findInjectAnnotation(someField, InjectProperty.class))
                .thenReturn(Optional.of(injectPropertyAnnotation));
        when(propertiesContainer.getPropertyValue(injectPropertyAnnotation.value(), String.class))
                .thenReturn(dependencyValue);

        var resolvedDependency = propertyDependencyResolver.resolveDependency(dependencyDefinition);

        if(resolvedDependency.isPresent() && resolvedDependency.get() instanceof String resolvedString) {
            assertEquals(dependencyValue, resolvedString);
        } else {
            fail("Resolved dependency is not a String");

        }
    }

    @Test
    public void shouldNotResolveDependency_whenNotPresentInProperties() {
        when(injectAnnotationScanner.findInjectAnnotation(someField, InjectProperty.class))
                .thenReturn(Optional.of(injectPropertyAnnotation));
        when(propertiesContainer.getPropertyValue(injectPropertyAnnotation.value(), String.class))
                .thenThrow(new RuntimeException("Dependency not found"));

        var resolvedDependency = propertyDependencyResolver.resolveDependency(dependencyDefinition);

        assertTrue(resolvedDependency.isEmpty());
    }

    @Test
    public void shouldNotResolveDependency_whenNotAnnotatedWithInjectProperty() {
        when(injectAnnotationScanner.findInjectAnnotation(someField, InjectProperty.class))
                .thenReturn(Optional.empty());

        var resolvedDependency = propertyDependencyResolver.resolveDependency(dependencyDefinition);

        assertTrue(resolvedDependency.isEmpty());
    }

    @InjectProperty("someString")
    private String someString;

}
