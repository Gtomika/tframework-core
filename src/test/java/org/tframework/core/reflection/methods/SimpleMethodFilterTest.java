/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.tframework.core.annotations.AnnotationScanner;
import org.tframework.core.annotations.AnnotationScannersFactory;
import org.tframework.core.annotations.MultipleAnnotationsScannedException;

class SimpleMethodFilterTest {

    private final SimpleMethodFilter simpleMethodFilter = new SimpleMethodFilter();
    private final AnnotationScanner annotationScanner = AnnotationScannersFactory.createComposedAnnotationScanner();

    @Test
    public void shouldFilterByAnnotation_whenNotStrict() {
        var methods = getMethods();
        var results = simpleMethodFilter.filterByAnnotation(methods, TestAnnotation.class, annotationScanner, false);

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().getName().equals("testMethod1")));
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().getName().equals("testMethod3")));
    }

    @Test
    public void shouldFilterByAnnotation_whenStrict() {
        var methods = getMethods();
        assertThrows(MultipleAnnotationsScannedException.class, () -> {
            simpleMethodFilter.filterByAnnotation(methods, TestAnnotation.class, annotationScanner, true);
        });
    }

    private Set<Method> getMethods() {
        return Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(m -> m.getName().startsWith("testMethod"))
                .collect(Collectors.toSet());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation {}

    @TestAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    @interface AnotherTestAnnotation {}


    @TestAnnotation
    private void testMethod1() {}

    protected int testMethod2() { return 0; }

    @TestAnnotation
    @AnotherTestAnnotation //this is also a test annotation
    public String testMethod3() { return ""; }

}
