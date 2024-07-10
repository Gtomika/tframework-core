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
package org.tframework.core.elements;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.tframework.core.elements.annotations.Priority;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.source.ClassElementSource;
import org.tframework.core.reflection.annotations.AnnotationScanner;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class PriorityAnnotationComparatorTest {

    @Mock
    private AnnotationScanner annotationScanner;

    private PriorityAnnotationComparator comparator;

    @BeforeEach
    public void setUp() throws Exception {
        comparator = new PriorityAnnotationComparator(annotationScanner);

        when(annotationScanner.scanOneStrict(TestClass1.class, Priority.class))
                .thenReturn(Optional.of(TestClass1.class.getAnnotation(Priority.class)));
        when(annotationScanner.scanOneStrict(TestClass2.class, Priority.class))
                .thenReturn(Optional.of(TestClass2.class.getAnnotation(Priority.class)));
        when(annotationScanner.scanOneStrict(TestClass3.class, Priority.class))
                .thenReturn(Optional.empty());
    }

    @Test
    public void shouldCompareCorrectly_whenBothObjectsAreAnnotated() throws Exception {
        var e1 = mockedContext(TestClass1.class);
        var e2 = mockedContext(TestClass2.class);

        var result = comparator.compare(e1, e2);
        assertTrue(result > 0);
    }

    @Test
    public void shouldCompareCorrectly_whenOneObjectIsAnnotated() throws Exception {
        var e1 = mockedContext(TestClass1.class);
        var e2 = mockedContext(TestClass3.class);

        var result = comparator.compare(e1, e2);
        assertTrue(result < 0);
    }

    @Priority(1)
    static class TestClass1 {
        public TestClass1() {}
    }

    @Priority(2)
    static class TestClass2 {
        public TestClass2() {}
    }

    /**
     * Defaults to {@link Priority#DEFAULT}
     */
    static class TestClass3 {
        public TestClass3() {}
    }

    private ElementContext mockedContext(Class<?> sourceClass) throws Exception {
        var mockContext = Mockito.mock(ElementContext.class);
        var elementSource = new ClassElementSource(sourceClass, sourceClass.getConstructor());
        when(mockContext.getSource()).thenReturn(elementSource);
        return mockContext;
    }
}
