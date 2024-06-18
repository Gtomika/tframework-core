/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.events.annotations.Subscribe;
import org.tframework.core.events.exception.EventSubscriptionException;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.methods.MethodFilter;
import org.tframework.core.reflection.methods.MethodInvoker;

@ExtendWith(MockitoExtension.class)
public class SubscribeElementPostProcessorTest {

    private static final String TEST_TOPIC = "test";

    @Mock
    private MethodFilter methodFilter;

    @Mock
    private MethodInvoker methodInvoker;

    @Mock
    private ElementContext elementContext;

    @Mock
    private AnnotationScanner annotationScanner;

    @Mock
    private EventManager eventManager;

    private SubscribeElementPostProcessor postProcessor;

    private Method validSubscribeMethod;
    private Method invalidSubscribeMethod;
    private Method otherMethod;

    @BeforeEach
    public void setUp() throws Exception {
        postProcessor = new SubscribeElementPostProcessor(
                annotationScanner, methodFilter, methodInvoker, eventManager
        );
        validSubscribeMethod = this.getClass().getMethod("validSubscribe", Object.class);
        invalidSubscribeMethod = this.getClass().getMethod("invalidSubscribe");
        otherMethod = this.getClass().getMethod("otherStuff");
    }

    @Test
    public void shouldNotSubscribe_whenMethodIsNotAnnotated() {
        when(elementContext.getMethods()).thenReturn(Set.of(otherMethod));
        when(methodFilter.filterByAnnotation(
                elementContext.getMethods(),
                Subscribe.class,
                annotationScanner,
                true
        )).thenReturn(Set.of());

        postProcessor.postProcessInstance(elementContext, this);

        verify(eventManager, never()).subscribe(any(), any());
    }

    @Test
    public void shouldNotSubscribe_whenMethodIsNotValid() {
        when(elementContext.getMethods()).thenReturn(Set.of(invalidSubscribeMethod));
        when(methodFilter.filterByAnnotation(
                elementContext.getMethods(),
                Subscribe.class,
                annotationScanner,
                true
        )).thenReturn(Set.of(
                new AnnotationFilteringResult<>(
                        invalidSubscribeMethod.getAnnotation(Subscribe.class),
                        invalidSubscribeMethod
                ))
        );
        mockMethodFilterAsInvalid(invalidSubscribeMethod);

        var exception = assertThrows(EventSubscriptionException.class, () -> {
            postProcessor.postProcessInstance(elementContext, this);
        });
        assertEquals(invalidSubscribeMethod, exception.getMethod());
        assertEquals(elementContext, exception.getElementContext());
        assertTrue(exception.getErrors().contains(SubscribeElementPostProcessor.METHOD_DOES_NOT_HAVE_EXACTLY_ONE_PARAMETER_ERROR));
    }

    @Test
    public void shouldSubscribe_whenMethodIsAnnotatedAndValid() {
        when(elementContext.getMethods()).thenReturn(Set.of(validSubscribeMethod));
        when(methodFilter.filterByAnnotation(
                elementContext.getMethods(),
                Subscribe.class,
                annotationScanner,
                true
        )).thenReturn(Set.of(
                new AnnotationFilteringResult<>(
                        validSubscribeMethod.getAnnotation(Subscribe.class),
                        validSubscribeMethod
                ))
        );
        mockMethodFilterAsValid(validSubscribeMethod);

        postProcessor.postProcessInstance(elementContext, this);

        verify(eventManager, times(1)).subscribe(eq(TEST_TOPIC), any());
    }

    private void mockMethodFilterAsValid(Method method) {
        when(methodFilter.isPublic(method)).thenReturn(true);
        when(methodFilter.isStatic(method)).thenReturn(false);
        when(methodFilter.isAbstract(method)).thenReturn(false);
        when(methodFilter.hasExactlyOneParameter(method)).thenReturn(true);
    }

    private void mockMethodFilterAsInvalid(Method method) {
        when(methodFilter.isPublic(method)).thenReturn(true);
        when(methodFilter.isStatic(method)).thenReturn(false);
        when(methodFilter.isAbstract(method)).thenReturn(false);
        when(methodFilter.hasExactlyOneParameter(method)).thenReturn(false); //<-- invalid
    }

    public void otherStuff() {}

    @Subscribe(TEST_TOPIC)
    public void validSubscribe(Object payload) {}

    @Subscribe(TEST_TOPIC) //no param
    public void invalidSubscribe() {}
}
