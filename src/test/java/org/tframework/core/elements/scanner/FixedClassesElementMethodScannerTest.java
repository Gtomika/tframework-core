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
package org.tframework.core.elements.scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.methods.MethodFilter;
import org.tframework.core.reflection.methods.MethodScanner;

@ExtendWith(MockitoExtension.class)
class FixedClassesElementMethodScannerTest {

    @Mock
    private MethodScanner methodScanner;

    @Mock
    private MethodFilter methodFilter;

    @Mock
    private AnnotationScanner annotationScanner;

    private FixedClassesElementMethodScanner scanner;

    @BeforeEach
    void setUp() {
        scanner = FixedClassesElementMethodScanner.builder()
                .methodScanner(methodScanner)
                .methodFilter(methodFilter)
                .annotationScanner(annotationScanner)
                .build();
    }

    @Test
    public void shouldScanMethodElements_ofClass() {
        var dummyMethods = Set.of(DummyClass.class.getDeclaredMethods());
        when(methodScanner.scanMethods(DummyClass.class)).thenReturn(dummyMethods);

        Collection<AnnotationFilteringResult<Element, Method>> filteredDummyElementMethods = dummyMethods.stream()
                .filter(method -> method.getName().equals("dummyElementMethod"))
                .map(method -> new AnnotationFilteringResult<>(method.getAnnotation(Element.class), method))
                .collect(Collectors.toSet());
        when(methodFilter.filterByAnnotation(dummyMethods, Element.class, annotationScanner, true))
                .thenReturn(filteredDummyElementMethods);

        scanner.setClassToScan(DummyClass.class);
        var results = scanner.scanElements();

        assertEquals(1, results.size());
        assertEquals("dummyElementMethod", results.iterator().next().annotationSource().getName());
    }

    static class DummyClass {

        @Element
        public void dummyElementMethod() {}

        public void dummyNonElementMethod() {}
    }
}
