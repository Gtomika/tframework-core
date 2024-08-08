/*
Copyright 2023 Tamas Gaspar

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
package org.tframework.core.reflection.annotations;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ExtendedAnnotationMatcherTest {

    //not mocked to significantly reduce test complexity
    private final RepeatedAnnotationHandler repeatedAnnotationHandler = RepeatedAnnotationHandlersFactory.create();
    private final ExtendedAnnotationMatcher matcher = new ExtendedAnnotationMatcher(repeatedAnnotationHandler);

    @TestAnnotationA("A on MatchWhenEquals")
    static class MatchWhenEquals {}

    @Test
    public void shouldMatchAnnotation_whenEquals() {
        TestAnnotationA annotationA = MatchWhenEquals.class.getAnnotation(TestAnnotationA.class);
        var result = matcher.matches(TestAnnotationA.class, annotationA);

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
        RepeatedTestAnnotationA repeatedAnnotationA = testClass.getAnnotation(RepeatedTestAnnotationA.class);
        var result = matcher.matches(TestAnnotationA.class, repeatedAnnotationA);

        assertTrue(result.matches());
        assertEquals(2, result.matchedAnnotations().size());
        assertTrue(result.matchedAnnotations().stream().anyMatch(a -> a.value().equals("A on MatchWhenRepeated #1")));
        assertTrue(result.matchedAnnotations().stream().anyMatch(a -> a.value().equals("A on MatchWhenRepeated #2")));
    }

    @TestAnnotationB("B on NoMatch")
    static class NoMatch {}

    @Test
    public void shouldNotMatch_forDifferentAnnotation() {
        TestAnnotationB annotationB = NoMatch.class.getAnnotation(TestAnnotationB.class);
        var result = matcher.matches(TestAnnotationA.class, annotationB);

        assertFalse(result.matches());
        assertEquals(0, result.matchedAnnotations().size());
    }
}
