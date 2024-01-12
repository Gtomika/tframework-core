/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.tframework.core.annotations.AnnotationScanner;
import org.tframework.core.annotations.AnnotationScannersFactory;

class SimpleMethodFilterTest {

    private final SimpleMethodFilter simpleMethodFilter = new SimpleMethodFilter();
    private final AnnotationScanner annotationScanner = AnnotationScannersFactory.createComposedAnnotationScanner();

    @Test
    public void shouldFilterByAnnotation() {
        var methods = Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(m -> m.getName().startsWith("testMethod"))
                .collect(Collectors.toSet());
        var filteredMethods = simpleMethodFilter.filterByAnnotation(methods, TestAnnotation.class, annotationScanner);

        assertEquals(2, filteredMethods.size());
        assertTrue(filteredMethods.stream().anyMatch(m -> m.getName().equals("testMethod1")));
        assertTrue(filteredMethods.stream().anyMatch(m -> m.getName().equals("testMethod3")));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation {}

    @TestAnnotation
    private void testMethod1() {}
    protected int testMethod2() { return 0; }
    @TestAnnotation
    public String testMethod3() { return ""; }

}
