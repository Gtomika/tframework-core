/* Licensed under Apache-2.0 2023. */
package org.tframework.core.annotations;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ExtendedAnnotationMatcherTest {

	private final ExtendedAnnotationMatcher matcher = new ExtendedAnnotationMatcher();

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
