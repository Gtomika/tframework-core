/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.postprocessing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.InjectAnnotationScanner;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolverAggregator;
import org.tframework.core.reflection.field.FieldFiltersFactory;
import org.tframework.core.reflection.field.FieldScannersFactory;
import org.tframework.core.reflection.field.FieldSettersFactory;

@ExtendWith(MockitoExtension.class)
public class FieldInjectionPostProcessorTest {

    private static final String RESOLVED_DEPENDENCY = "resolved_dependency";

    @Mock
    private InjectAnnotationScanner injectAnnotationScanner;

    @Mock
    private DependencyResolverAggregator dependencyResolver;

    @Mock
    private ElementContext elementContext;

    private FieldInjectionPostProcessor fieldInjectionPostProcessor;

    @BeforeEach
    public void setUp() throws Exception {
        when(elementContext.getName()).thenReturn("test-element");

        dependencyField = ValidElement.class.getDeclaredField("string1");
        nonDependencyField = ValidElement.class.getDeclaredField("string2");
        invalidDependencyField = InvalidElement.class.getDeclaredField("string1");

        fieldInjectionPostProcessor = FieldInjectionPostProcessor.builder()
                .injectAnnotationScanner(injectAnnotationScanner)
                .dependencyResolver(dependencyResolver)
                //field reflection dependencies are not mocked to greatly simplify test
                .fieldScanner(FieldScannersFactory.createDefaultFieldScanner())
                .fieldFilter(FieldFiltersFactory.createDefaultFieldFilter())
                .fieldSetter(FieldSettersFactory.createDefaultFieldSetter())
                .build();
    }

    @Test
    public void shouldInjectFieldDependencies() {
        doReturn(ValidElement.class).when(elementContext).getType();
        when(injectAnnotationScanner.hasAnyInjectAnnotations(dependencyField)).thenReturn(true);
        when(injectAnnotationScanner.hasAnyInjectAnnotations(nonDependencyField)).thenReturn(false);

        when(dependencyResolver.resolveDependency(
                DependencyDefinition.fromField(dependencyField),
                elementContext,
                ElementDependencyGraph.empty(),
                FieldInjectionPostProcessor.DEPENDENCY_DECLARED_AS_FIELD
        )).thenReturn(RESOLVED_DEPENDENCY);

        ValidElement instance = new ValidElement();

        fieldInjectionPostProcessor.postProcessInstance(elementContext, instance);

        assertEquals(RESOLVED_DEPENDENCY, instance.string1);
        assertNull(instance.string2);
    }

    @Test
    public void shouldThrowFieldInjectionException_whenAnnotatedFieldIsInvalid() {
        doReturn(InvalidElement.class).when(elementContext).getType();
        when(injectAnnotationScanner.hasAnyInjectAnnotations(invalidDependencyField)).thenReturn(true);

        InvalidElement instance = new InvalidElement();

        assertThrows(FieldInjectionException.class, () -> fieldInjectionPostProcessor.postProcessInstance(elementContext, instance));
    }

    @Test
    public void shouldThrowFieldInjectionException_whenDependencyResolutionFails() {
        doReturn(ValidElement.class).when(elementContext).getType();
        when(injectAnnotationScanner.hasAnyInjectAnnotations(dependencyField)).thenReturn(true);

        when(dependencyResolver.resolveDependency(
                DependencyDefinition.fromField(dependencyField),
                elementContext,
                ElementDependencyGraph.empty(),
                FieldInjectionPostProcessor.DEPENDENCY_DECLARED_AS_FIELD
        )).thenReturn(1); //the field is a String, but an int is resolved

        ValidElement instance = new ValidElement();

        assertThrows(FieldInjectionException.class, () -> fieldInjectionPostProcessor.postProcessInstance(elementContext, instance));
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
