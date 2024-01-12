/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.annotations.AnnotationScanner;

@ExtendWith(MockitoExtension.class)
class SimpleClassFilterTest {

    private final SimpleClassFilter simpleClassFilter = new SimpleClassFilter();

    @Mock
    private AnnotationScanner annotationScanner;

    @Test
    public void shouldFilterByAnnotation() {
        List<Class<?>> classes = List.of(TestFilterClass1.class, TestFilterClass2.class);

        when(annotationScanner.hasAnnotation(TestFilterClass1.class, TestAnnotation.class)).thenReturn(false);
        when(annotationScanner.hasAnnotation(TestFilterClass2.class, TestAnnotation.class)).thenReturn(true);
        var filteredClasses = simpleClassFilter.filterByAnnotation(classes, TestAnnotation.class, annotationScanner);

        assertEquals(1, filteredClasses.size());
        assertTrue(filteredClasses.stream().anyMatch(clazz -> clazz.getName().equals(TestFilterClass2.class.getName())));
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

    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation {}

    static class TestFilterClass1 implements TestInterface {}

    @TestAnnotation
    static class TestFilterClass2 {}

}
