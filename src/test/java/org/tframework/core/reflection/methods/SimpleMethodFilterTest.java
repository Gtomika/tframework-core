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
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;
import org.tframework.core.reflection.annotations.MultipleAnnotationsScannedException;

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

    @ParameterizedTest
    @MethodSource("getVisibilityTestMethods")
    public void shouldFilterByPublic(Method method, boolean isPublic) {
        assertEquals(isPublic, simpleMethodFilter.isPublic(method));
    }

    @ParameterizedTest
    @MethodSource("getStaticnessTestMethods")
    public void shouldFilterByStatic(Method method, boolean isStatic) {
        assertEquals(isStatic, simpleMethodFilter.isStatic(method));
    }

    @ParameterizedTest
    @MethodSource("getVoidnessTestMethods")
    public void shouldFilterByVoid(Method method, boolean isVoid) {
        assertEquals(isVoid, simpleMethodFilter.hasVoidReturnType(method));
    }

    @ParameterizedTest
    @MethodSource("getHasParamsTestMethods")
    public void shouldFilterByHasParams(Method method, boolean hasParams) {
        assertEquals(hasParams, simpleMethodFilter.hasParameters(method));
    }


    // ---------------------------------- filter by annotation ----------------------------------

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

    // ---------------------------------- is public ----------------------------------

    public String publicMethod() { return ""; }

    private int privateMethod() { return 0; }

    protected int protectedMethod() { return 0; }

    int packagePrivateMethod() { return 0; }

    public static Stream<Arguments> getVisibilityTestMethods() throws Exception {
        return Stream.of(
                Arguments.of(SimpleMethodFilterTest.class.getDeclaredMethod("publicMethod"), true),
                Arguments.of(SimpleMethodFilterTest.class.getDeclaredMethod("privateMethod"), false),
                Arguments.of(SimpleMethodFilterTest.class.getDeclaredMethod("protectedMethod"), false),
                Arguments.of(SimpleMethodFilterTest.class.getDeclaredMethod("packagePrivateMethod"), false)
        );
    }

    // ---------------------------------- is static ----------------------------------

    private static int staticMethod() { return 0; }

    private int nonStaticMethod() { return 0; }

    public static Stream<Arguments> getStaticnessTestMethods() throws Exception {
        return Stream.of(
                Arguments.of(SimpleMethodFilterTest.class.getDeclaredMethod("staticMethod"), true),
                Arguments.of(SimpleMethodFilterTest.class.getDeclaredMethod("nonStaticMethod"), false)
        );
    }

    // ---------------------------------- has void return type ----------------------------------

    public void primitiveVoidMethod() {}

    public Void wrapperVoidMethod() { return null; }

    public String nonVoidMethod() { return ""; }

    public static Stream<Arguments> getVoidnessTestMethods() throws Exception {
        return Stream.of(
                Arguments.of(SimpleMethodFilterTest.class.getDeclaredMethod("primitiveVoidMethod"), true),
                Arguments.of(SimpleMethodFilterTest.class.getDeclaredMethod("wrapperVoidMethod"), true),
                Arguments.of(SimpleMethodFilterTest.class.getDeclaredMethod("nonVoidMethod"), false)
        );
    }

    // ----------------------------- has params -----------------------------

    public void methodWithParams(String s1, String s2) {}

    public void methodWithNoParams() {}

    public static Stream<Arguments> getHasParamsTestMethods() throws Exception {
        return Stream.of(
                Arguments.of(SimpleMethodFilterTest.class.getDeclaredMethod("methodWithParams"), true),
                Arguments.of(SimpleMethodFilterTest.class.getDeclaredMethod("methodWithNoParams"), false)
        );
    }
}
