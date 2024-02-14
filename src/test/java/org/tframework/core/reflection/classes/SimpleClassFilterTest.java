/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.classes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.annotations.MultipleAnnotationsScannedException;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleClassFilterTest {

    private final SimpleClassFilter simpleClassFilter = new SimpleClassFilter();

    @Mock
    private AnnotationScanner annotationScanner;

    @Test
    public void shouldFilterByAnnotation_whenNotStrict() {
        when(annotationScanner.scanOne(TestFilterClass1.class, TestAnnotation.class)).thenReturn(Optional.empty());

        TestAnnotation annotation = TestFilterClass2.class.getAnnotation(TestAnnotation.class);
        when(annotationScanner.scanOne(TestFilterClass2.class, TestAnnotation.class)).thenReturn(Optional.of(annotation));

        var classes = List.of(TestFilterClass1.class, TestFilterClass2.class);
        var filteringResults = simpleClassFilter.filterByAnnotation(classes, TestAnnotation.class, annotationScanner, false);

        assertEquals(1, filteringResults.size());
        var result = filteringResults.iterator().next();
        assertEquals(annotation, result.annotation());
        assertEquals(TestFilterClass2.class, result.annotationSource());
    }

    @Test
    public void shouldFilterByAnnotation_whenStrict() {
        when(annotationScanner.scanOneStrict(TestFilterClass1.class, TestAnnotation.class)).thenReturn(Optional.empty());

        var testAnnotations = Arrays.asList(TestFilterClass3.class.getAnnotationsByType(TestAnnotation.class));
        when(annotationScanner.scanOneStrict(TestFilterClass3.class, TestAnnotation.class))
                .thenThrow(new MultipleAnnotationsScannedException(TestFilterClass3.class, testAnnotations));

        var classes = List.of(TestFilterClass1.class, TestFilterClass3.class);

        assertThrows(MultipleAnnotationsScannedException.class, () -> {
            simpleClassFilter.filterByAnnotation(classes, TestAnnotation.class, annotationScanner, true);
        });
    }

    @Test
    public void shouldFilterByInterface() {
        List<Class<?>> classes = List.of(TestFilterClass1.class, TestFilterClass2.class);
        var filteredClasses = simpleClassFilter.filterBySuperClass(classes, TestInterface.class);

        assertEquals(1, filteredClasses.size());
        assertTrue(filteredClasses.stream().anyMatch(clazz -> clazz.getName().equals(TestFilterClass1.class.getName())));
    }

    @Test
    public void shouldFilterBySuperClass() {
        List<Class<?>> classes = List.of(String.class, Integer.class, Double.class);
        var filteredClasses = simpleClassFilter.filterBySuperClass(classes, Number.class);

        assertEquals(2, filteredClasses.size());
        assertTrue(filteredClasses.stream().anyMatch(clazz -> clazz.equals(Integer.class)));
        assertTrue(filteredClasses.stream().anyMatch(clazz -> clazz.equals(Double.class)));
    }

    interface TestInterface {}

    @Repeatable(TestAnnotationContainer.class)
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation {}

    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotationContainer {
        TestAnnotation[] value();
    }

    static class TestFilterClass1 implements TestInterface {}

    @TestAnnotation
    static class TestFilterClass2 {}

    @TestAnnotation
    @TestAnnotation
    static class TestFilterClass3 {}

}
