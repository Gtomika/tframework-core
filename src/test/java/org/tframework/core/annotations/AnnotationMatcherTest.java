package org.tframework.core.annotations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationMatcherTest {

    @TestAnnotationA("A on MatchWhenEquals")
    static class MatchWhenEquals {}

    @Test
    public void shouldMatchAnnotation_whenEquals() {
        var matcher = new AnnotationMatcher<>(TestAnnotationA.class);

        TestAnnotationA annotationA = MatchWhenEquals.class.getAnnotation(TestAnnotationA.class);
        var result = matcher.matches(annotationA);

        assertTrue(result.matches());
        assertEquals(1, result.matchedAnnotations().size());
        assertEquals("A on MatchWhenEquals", result.matchedAnnotations().getFirst().value());
    }

    @TestAnnotationA("A on MatchWhenRepeated #1")
    @TestAnnotationA("A on MatchWhenRepeated #2")
    static class MatchWhenRepeated {}

    @RepeatedTestAnnotationA(value = {
            @TestAnnotationA("A on MatchWhenRepeated #1"),
            @TestAnnotationA("A on MatchWhenRepeated #2")
    })
    static class MatchWhenRepeatedContaining {}

    @ParameterizedTest
    @ValueSource(classes = {MatchWhenRepeated.class, MatchWhenRepeatedContaining.class})
    public void shouldMatchAnnotation_whenRepeated(Class<?> testClass) {
        var matcher = new AnnotationMatcher<>(TestAnnotationA.class);

        RepeatedTestAnnotationA annotationA = testClass.getAnnotation(RepeatedTestAnnotationA.class);
        var result = matcher.matches(annotationA);

        assertTrue(result.matches());
        assertEquals(2, result.matchedAnnotations().size());
        assertTrue(result.matchedAnnotations().stream().anyMatch(a -> a.value().equals("A on MatchWhenRepeated #1")));
        assertTrue(result.matchedAnnotations().stream().anyMatch(a -> a.value().equals("A on MatchWhenRepeated #2")));
    }

    @TestAnnotationB("B on NoMatch")
    static class NoMatch {}

    @Test
    public void shouldNotMatch_forDifferentAnnotation() {
        var matcher = new AnnotationMatcher<>(TestAnnotationA.class);

        TestAnnotationB annotationB = NoMatch.class.getAnnotation(TestAnnotationB.class);
        var result = matcher.matches(annotationB);

        assertFalse(result.matches());
        assertEquals(0, result.matchedAnnotations().size());
    }
}