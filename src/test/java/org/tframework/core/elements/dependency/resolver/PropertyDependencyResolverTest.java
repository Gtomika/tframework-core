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
    private Field someFieldWithDefaultValue;

    private InjectProperty injectPropertyAnnotation;
    private InjectProperty injectPropertyAnnotationWithDefaultValue;

    private DependencyDefinition dependencyDefinition;
    private DependencyDefinition dependencyDefinitionWithDefaultValue;

    @BeforeEach
    void setUp() throws Exception {
        propertyDependencyResolver = new PropertyDependencyResolver(propertiesContainer, injectAnnotationScanner);

        someField = this.getClass().getDeclaredField("someString");
        injectPropertyAnnotation = someField.getAnnotation(InjectProperty.class);

        someFieldWithDefaultValue = this.getClass().getDeclaredField("someString2");
        injectPropertyAnnotationWithDefaultValue = someFieldWithDefaultValue.getAnnotation(InjectProperty.class);

        dependencyDefinition = new DependencyDefinition(someField, someField.getType());
        dependencyDefinitionWithDefaultValue = new DependencyDefinition(someFieldWithDefaultValue, someFieldWithDefaultValue.getType());
    }

    @Test
    public void shouldResolveDependency_whenPresentInProperties() {
        String dependencyValue = "test";
        when(injectAnnotationScanner.findInjectAnnotation(someField, InjectProperty.class))
                .thenReturn(Optional.of(injectPropertyAnnotation));
        when(propertiesContainer.getPropertyValueNonGeneric(injectPropertyAnnotation.value(), String.class))
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
        when(propertiesContainer.getPropertyValueNonGeneric(injectPropertyAnnotation.value(), String.class))
                .thenThrow(new RuntimeException("Dependency not found"));

        var resolvedDependency = propertyDependencyResolver.resolveDependency(dependencyDefinition);

        assertTrue(resolvedDependency.isEmpty());
    }

    @Test
    public void shouldResolveDependency_whenNotPresentInProperties_butHasDefaultValue() {
        String dependencyValue = "test";
        when(injectAnnotationScanner.findInjectAnnotation(someFieldWithDefaultValue, InjectProperty.class))
                .thenReturn(Optional.of(injectPropertyAnnotationWithDefaultValue));
        when(propertiesContainer.getPropertyValueNonGeneric(
                injectPropertyAnnotationWithDefaultValue.value(),
                String.class,
                injectPropertyAnnotationWithDefaultValue.defaultValue()
        )).thenReturn(dependencyValue);

        var resolvedDependency = propertyDependencyResolver.resolveDependency(dependencyDefinitionWithDefaultValue);

        if(resolvedDependency.isPresent() && resolvedDependency.get() instanceof String resolvedString) {
            assertEquals(dependencyValue, resolvedString);
        } else {
            fail("Resolved dependency is not a String");
        }
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

    @InjectProperty(value = "someString2", defaultValue = "default")
    private String someString2;

}
