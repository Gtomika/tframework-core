/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.tframework.core.annotations.AnnotationScannersFactory;
import org.tframework.core.di.annotations.Element;
import org.tframework.core.reflection.methods.MethodFiltersFactory;
import org.tframework.core.reflection.methods.MethodScannersFactory;

class FixedClassesElementMethodScannerTest {

    /*
    Maybe mocking the dependencies of FixedClassesElementMethodScanner would be a better idea?
     */
    private final FixedClassesElementMethodScanner scanner = FixedClassesElementMethodScanner.builder()
            .classesToScan(Set.of(DummyClass.class))
            .methodScanner(MethodScannersFactory.createDefaultMethodScanner())
            .methodFilter(MethodFiltersFactory.createDefaultMethodFilter())
            .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
            .build();

    @Test
    public void shouldScanMethodElements_fromFixedClasses() {
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
