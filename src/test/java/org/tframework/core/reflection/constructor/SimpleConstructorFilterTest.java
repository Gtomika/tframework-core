/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.jupiter.api.Test;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;
import org.tframework.core.reflection.annotations.MultipleAnnotationsScannedException;

class SimpleConstructorFilterTest {

    //maybe this should be mocked?
    private final AnnotationScanner annotationScanner = AnnotationScannersFactory.createComposedAnnotationScanner();
    private final ConstructorScanner constructorScanner = new SimpleConstructorScanner();

    private final SimpleConstructorFilter filter = new SimpleConstructorFilter();

    @Test
    public void shouldFilterByAnnotation_whenNotStrict() {
        var allConstructors = constructorScanner.getAllConstructors(TestClass.class);
        assertEquals(3, allConstructors.size());

        var results = filter.filterByAnnotation(allConstructors, CoolConstructorAnnotation.class, annotationScanner, false);
        assertEquals(1, results.size());
        results.forEach(result -> {
            var parameters = result.annotationSource().getParameters();
            assertEquals(1, parameters.length);
            assertEquals(Integer.class, parameters[0].getType());
        });
    }

    @Test
    public void shouldThrowException_whenFilteringAndMultipleAnnotationsFound_whenStrict() {
        var allConstructors = constructorScanner.getAllConstructors(TestClass.class);
        assertEquals(3, allConstructors.size());

        assertThrows(MultipleAnnotationsScannedException.class, () -> {
            filter.filterByAnnotation(allConstructors, CoolConstructorAnnotation.class, annotationScanner, true);
        });
    }

    @Test
    public void shouldFilterPublicConstructors() {
        var allConstructors = constructorScanner.getAllConstructors(TestClass.class);
        assertEquals(3, allConstructors.size());

        var publicConstructors = filter.filterPublicConstructors(allConstructors);
        assertEquals(1, publicConstructors.size());
        publicConstructors.forEach(publicConstructor -> {
            assertEquals(0, publicConstructor.getParameters().length);
        });
    }

    static class TestClass {

        public TestClass() {}

        @CoolConstructorAnnotation
        @CoolConstructorAnnotation2
        private TestClass(Integer i) {} //strict mode does not allow this

        TestClass(String s) {}
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface CoolConstructorAnnotation {}

    @CoolConstructorAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    @interface CoolConstructorAnnotation2 {}

}
