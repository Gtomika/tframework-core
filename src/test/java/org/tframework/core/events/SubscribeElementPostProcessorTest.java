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
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.tframework.core.elements.postprocessing.PostProcessorBaseTest;
import org.tframework.core.events.annotations.Subscribe;
import org.tframework.core.events.exception.EventSubscriptionException;
import org.tframework.core.reflection.annotations.PreScannedAnnotations;
import org.tframework.core.reflection.methods.MethodFilter;
import org.tframework.core.reflection.methods.MethodInvoker;

public class SubscribeElementPostProcessorTest extends PostProcessorBaseTest {

    private static final String TEST_TOPIC = "test";

    @Mock
    private MethodFilter methodFilter;

    @Mock
    private MethodInvoker methodInvoker;

    @Mock
    private EventManager eventManager;

    private SubscribeElementPostProcessor postProcessor;

    private Method validSubscribeMethod;
    private Method invalidSubscribeMethod;
    private Method otherMethod;

    @BeforeEach
    public void setUp() throws Exception {
        postProcessor = new SubscribeElementPostProcessor(methodFilter, methodInvoker, eventManager);
        validSubscribeMethod = this.getClass().getMethod("validSubscribe", Object.class);
        invalidSubscribeMethod = this.getClass().getMethod("invalidSubscribe");
        otherMethod = this.getClass().getMethod("otherStuff");
    }

    @Test
    public void shouldNotSubscribe_whenMethodIsNotAnnotated() {
        setUpElementContextPreScanning(otherMethod);

        postProcessor.postProcessInstance(application, elementContext, this);

        verify(eventManager, never()).subscribe(any(), any());
    }

    @Test
    public void shouldNotSubscribe_whenMethodIsNotValid() {
        setUpElementContextPreScanning(invalidSubscribeMethod);
        mockMethodFilterAsInvalid(invalidSubscribeMethod);

        var exception = assertThrows(EventSubscriptionException.class, () ->
                postProcessor.postProcessInstance(application, elementContext, this));
        assertEquals(invalidSubscribeMethod, exception.getMethod());
        assertEquals(elementContext, exception.getElementContext());
        assertTrue(exception.getErrors().contains(SubscribeElementPostProcessor.METHOD_DOES_NOT_HAVE_EXACTLY_ONE_PARAMETER_ERROR));
    }

    @Test
    public void shouldSubscribe_whenMethodIsAnnotatedAndValid() {
       setUpElementContextPreScanning(validSubscribeMethod);
        mockMethodFilterAsValid(validSubscribeMethod);

        postProcessor.postProcessInstance(application, elementContext, this);

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


    private void setUpElementContextPreScanning(Method method) {
        var methodsAnnotationData = Map.of(method,
                createPreScanningData(method, method.getAnnotation(Subscribe.class)));
        when(elementContext.getAnnotationsOnMethods()).thenReturn(methodsAnnotationData);
    }

    private PreScannedAnnotations createPreScanningData(Method method, Subscribe subscribeAnnotation) {
        var annotations = PreScannedAnnotations.empty(method);
        annotations.add(subscribeAnnotation);
        return annotations;
    }
}
