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
package org.tframework.core.elements.postprocessing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.InjectAnnotationHelper;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolverAggregator;
import org.tframework.core.reflection.annotations.PreScannedAnnotations;
import org.tframework.core.reflection.field.SimpleFieldFilter;
import org.tframework.core.reflection.field.SimpleFieldSetter;

public class FieldInjectionPostProcessorTest extends PostProcessorBaseTest {

    private static final String RESOLVED_DEPENDENCY = "resolved_dependency";

    @Mock
    private InjectAnnotationHelper injectAnnotationHelper;

    @Mock
    private PreScannedAnnotations dependencyPreScannedAnnotations;

    @Mock
    private PreScannedAnnotations nonDependencyPreScannedAnnotations;

    @Mock
    private DependencyResolverAggregator dependencyResolver;

    private FieldInjectionPostProcessor fieldInjectionPostProcessor;

    @BeforeEach
    public void setUp() throws Exception {
        when(elementContext.getName()).thenReturn("test-element");

        dependencyField = ValidElement.class.getDeclaredField("string1");
        nonDependencyField = ValidElement.class.getDeclaredField("string2");
        invalidDependencyField = InvalidElement.class.getDeclaredField("string1");

        fieldInjectionPostProcessor = FieldInjectionPostProcessor.builder()
                .injectAnnotationHelper(injectAnnotationHelper)
                .dependencyResolver(dependencyResolver)
                //field reflection dependencies are not mocked to greatly simplify test
                .fieldFilter(new SimpleFieldFilter())
                .fieldSetter(new SimpleFieldSetter())
                .build();
    }

    @Test
    public void shouldInjectFieldDependencies() {
        var fieldAnnotationData = Map.of(
                dependencyField, dependencyPreScannedAnnotations,
                nonDependencyField, nonDependencyPreScannedAnnotations
        );
        when(elementContext.getAnnotationsOnFields()).thenReturn(fieldAnnotationData);
        when(injectAnnotationHelper.hasAnyInjectAnnotations(dependencyPreScannedAnnotations)).thenReturn(true);
        when(injectAnnotationHelper.hasAnyInjectAnnotations(nonDependencyPreScannedAnnotations)).thenReturn(false);

        when(dependencyResolver.resolveDependency(
                DependencyDefinition.fromField(dependencyField),
                elementContext,
                ElementDependencyGraph.empty(),
                dependencyPreScannedAnnotations,
                FieldInjectionPostProcessor.DEPENDENCY_DECLARED_AS_FIELD
        )).thenReturn(RESOLVED_DEPENDENCY);

        ValidElement instance = new ValidElement();

        fieldInjectionPostProcessor.postProcessInstance(application, elementContext, instance);

        assertEquals(RESOLVED_DEPENDENCY, instance.string1);
        assertNull(instance.string2);
    }

    @Test
    public void shouldThrowFieldInjectionException_whenAnnotatedFieldIsInvalid() {
        var fieldAnnotationData = Map.of(dependencyField, dependencyPreScannedAnnotations);
        when(elementContext.getAnnotationsOnFields()).thenReturn(fieldAnnotationData);
        when(injectAnnotationHelper.hasAnyInjectAnnotations(dependencyPreScannedAnnotations)).thenReturn(true);

        InvalidElement instance = new InvalidElement();

        assertThrows(FieldInjectionException.class, () ->
                fieldInjectionPostProcessor.postProcessInstance(application, elementContext, instance));
    }

    @Test
    public void shouldThrowFieldInjectionException_whenDependencyResolutionFails() {
        var fieldAnnotationData = Map.of(dependencyField, dependencyPreScannedAnnotations );
        when(elementContext.getAnnotationsOnFields()).thenReturn(fieldAnnotationData);
        when(injectAnnotationHelper.hasAnyInjectAnnotations(dependencyPreScannedAnnotations)).thenReturn(true);

        when(dependencyResolver.resolveDependency(
                DependencyDefinition.fromField(dependencyField),
                elementContext,
                ElementDependencyGraph.empty(),
                dependencyPreScannedAnnotations,
                FieldInjectionPostProcessor.DEPENDENCY_DECLARED_AS_FIELD
        )).thenReturn(1); //the field is a String, but an int is resolved

        ValidElement instance = new ValidElement();

        assertThrows(FieldInjectionException.class, () ->
                fieldInjectionPostProcessor.postProcessInstance(application, elementContext, instance));
    }

    static class ValidElement {

        @InjectElement
        private String string1;

        private String string2;

    }

    static class InvalidElement {

        @InjectElement
        private static String string1;

    }

    private Field dependencyField;
    private Field nonDependencyField;
    private Field invalidDependencyField;

}
