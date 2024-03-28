/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.postprocessing;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.postprocessing.annotations.PostInitialization;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.methods.MethodFilter;
import org.tframework.core.reflection.methods.MethodInvoker;
import org.tframework.core.reflection.methods.MethodScanner;

@ExtendWith(MockitoExtension.class)
public class PostInitializationMethodPostProcessorTest {

    @Mock
    private ElementContext elementContext;

    @Mock
    private AnnotationScanner annotationScanner;

    @Mock
    private MethodScanner methodScanner;

    @Mock
    private MethodFilter methodFilter;

    @Mock
    private MethodInvoker methodInvoker;

    @InjectMocks
    private PostInitializationMethodPostProcessor processor;

    @PostInitialization
    public void postInit1() {}
    private Method postInit1Method;

    @PostInitialization
    public void postInit2() {}
    private Method postInit2Method;


    @BeforeEach
    public void setUp() throws Exception {
        when(elementContext.getName()).thenReturn("test-element");
        doReturn(this.getClass()).when(elementContext).getType();

        postInit1Method = this.getClass().getDeclaredMethod("postInit1");
        postInit2Method = this.getClass().getDeclaredMethod("postInit2");
    }

    @Test
    public void shouldInvokeValidPostInitializationMethod() {
        var methods = Set.of(postInit1Method, postInit2Method);
        when(methodScanner.scanMethods(this.getClass())).thenReturn(methods);
        mockFilteringByAnnotation(methods);
        mockThatPostInitMethodIsValid(methods);

        processor.postProcessInstance(elementContext, this);
        verifyMethodsInvoked(methods);
    }

    @Test
    public void shouldThrowException_whenPostInitializationMethodIsInvalid() {
        var methods = Set.of(postInit1Method);
        when(methodScanner.scanMethods(this.getClass())).thenReturn(methods);
        mockFilteringByAnnotation(methods);
        mockThatPostInitMethodIsInvalid(methods);

        assertThrows(PostInitializationMethodException.class, () -> processor.postProcessInstance(elementContext, this));
    }

    @Test
    public void shouldThrowException_whenPostInitializationMethodIsValid_butCannotBeExecuted() {
        var methods = Set.of(postInit1Method);
        when(methodScanner.scanMethods(this.getClass())).thenReturn(methods);
        mockFilteringByAnnotation(methods);
        mockThatPostInitMethodIsValid(methods);
        mockPostInitMethodThrowingAnException(postInit1Method);

        assertThrows(PostInitializationMethodException.class, () -> processor.postProcessInstance(elementContext, this));
    }

    private void mockFilteringByAnnotation(Set<Method> methods) {
        var filteringResults = toFilteringResults(methods);
        when(methodFilter.filterByAnnotation(methods, PostInitialization.class, annotationScanner, false))
                .thenReturn(filteringResults);
    }

    private Set<AnnotationFilteringResult<PostInitialization, Method>> toFilteringResults(Set<Method> methods) {
        return methods.stream()
                .map(method -> {
                    var annotation = method.getAnnotation(PostInitialization.class);
                    return new AnnotationFilteringResult<>(annotation, method);
                })
                .collect(Collectors.toSet());
    }

    private void mockThatPostInitMethodIsValid(Set<Method> methods) {
        methods.forEach(method -> {
            when(methodFilter.isPublic(method)).thenReturn(true);
            when(methodFilter.isStatic(method)).thenReturn(false);
            when(methodFilter.isAbstract(method)).thenReturn(false);
            when(methodFilter.hasParameters(method)).thenReturn(false);
        });
    }

    private void mockThatPostInitMethodIsInvalid(Set<Method> methods) {
        methods.forEach(method -> {
            when(methodFilter.isPublic(method)).thenReturn(false);
            when(methodFilter.isStatic(method)).thenReturn(true);
            when(methodFilter.isAbstract(method)).thenReturn(true);
            when(methodFilter.hasParameters(method)).thenReturn(true);
        });
    }

    private void mockPostInitMethodThrowingAnException(Method method) {
        doThrow(new RuntimeException("I die")).when(methodInvoker)
                        .invokeMethodWithNoParametersAndIgnoreResult(this, method);
    }

    private void verifyMethodsInvoked(Set<Method> methods) {
        methods.forEach(method -> verify(methodInvoker, times(1))
                .invokeMethodWithNoParametersAndIgnoreResult(this, method));
    }

}
