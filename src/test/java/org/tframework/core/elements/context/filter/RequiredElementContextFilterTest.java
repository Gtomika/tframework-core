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
package org.tframework.core.elements.context.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.Application;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.filter.annotation.RequiredElement;
import org.tframework.core.elements.context.filter.exception.ElementFilterException;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.reflection.annotations.AnnotationScanner;

@ExtendWith(MockitoExtension.class)
public class RequiredElementContextFilterTest {

    @Mock
    private AnnotationScanner annotationScanner;

    @Mock
    private ElementContext elementContext;

    @Mock
    private ElementsContainer elementsContainer;

    @Mock
    private ElementSource elementSource;

    private RequiredElementContextFilter filter;
    private AnnotatedElement annotatedElementSource;
    private Application application;

    @BeforeEach
    public void setUp() {
        annotatedElementSource = this.getClass();
        when(elementContext.getSource()).thenReturn(elementSource);
        when(elementSource.annotatedSource()).thenReturn(annotatedElementSource);

        filter = new RequiredElementContextFilter(annotationScanner);
        application = Application.builder()
                .elementsContainer(elementsContainer)
                .build();
    }

    @Test
    public void shouldKeepElementContext_whenNoRequiredElementAnnotation() {
        givenRequiredElementAnnotations(Collections.emptyList());
        boolean result = filter.discardElementContext(elementContext, application);
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalidNoAttributes", "invalidBothAttributes"})
    public void shouldThrowException_whenInvalidRequiredElementAnnotation(String fieldName) throws Exception {
        RequiredElement annotation = givenAnnotation(fieldName);
        givenRequiredElementAnnotations(Collections.singletonList(annotation));
        assertThrows(ElementFilterException.class, () -> filter.discardElementContext(elementContext, application));
    }

    @Test
    public void shouldKeepElementContext_whenValidRequiredElementAnnotationWithName() throws Exception {
        RequiredElement annotation = givenAnnotation("validName");
        givenRequiredElementAnnotations(Collections.singletonList(annotation));
        when(elementsContainer.hasElementContext(annotation.name())).thenReturn(true);
        boolean result = filter.discardElementContext(elementContext, application);
        assertFalse(result);
    }

    @Test
    public void shouldKeepElementContext_whenValidRequiredElementAnnotationWithType() throws Exception {
        RequiredElement annotation = givenAnnotation("validType");
        givenRequiredElementAnnotations(Collections.singletonList(annotation));
        when(elementsContainer.hasElementContext(annotation.type())).thenReturn(true);
        boolean result = filter.discardElementContext(elementContext, application);
        assertFalse(result);
    }

    @Test
    public void shouldKeepElementContext_whenMultipleAnnotations_andAllFulfilled() throws Exception {
        RequiredElement annotation1 = givenAnnotation("validName");
        RequiredElement annotation2 = givenAnnotation("validType");
        givenRequiredElementAnnotations(List.of(annotation1, annotation2));
        when(elementsContainer.hasElementContext(annotation1.name())).thenReturn(true);
        when(elementsContainer.hasElementContext(annotation2.type())).thenReturn(true);
        boolean result = filter.discardElementContext(elementContext, application);
        assertFalse(result);
    }

    @Test
    public void shouldDiscardElementContext_whenMultipleAnnotations_andOneNotFulfilled() throws Exception {
        RequiredElement annotation1 = givenAnnotation("validName");
        RequiredElement annotation2 = givenAnnotation("validType");
        givenRequiredElementAnnotations(List.of(annotation1, annotation2));
        when(elementsContainer.hasElementContext(annotation1.name())).thenReturn(true);
        when(elementsContainer.hasElementContext(annotation2.type())).thenReturn(false);
        boolean result = filter.discardElementContext(elementContext, application);
        assertTrue(result);
    }

    private void givenRequiredElementAnnotations(List<RequiredElement> requiredElements) {
        when(annotationScanner.scan(annotatedElementSource, RequiredElement.class))
                .thenReturn(requiredElements);
    }

    private RequiredElement givenAnnotation(String fieldName) throws Exception {
        return this.getClass().getDeclaredField(fieldName).getAnnotation(RequiredElement.class);
    }

    @RequiredElement
    private String invalidNoAttributes;

    @RequiredElement(name = "foo", type = String.class)
    private String invalidBothAttributes;

    @RequiredElement(name = "foo")
    private String validName;

    @RequiredElement(type = String.class)
    private String validType;
}
