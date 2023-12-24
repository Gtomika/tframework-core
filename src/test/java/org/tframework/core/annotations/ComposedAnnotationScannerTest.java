/* Licensed under Apache-2.0 2023. */
package org.tframework.core.annotations;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Retention;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ComposedAnnotationScannerTest {

	/*
	This may not be the best approach, but instead of using a mock annotation matcher, I decided to
	use a concrete implementation, to reduce mocking noise in these tests.
	 */
	private final ExtendedAnnotationMatcher extendedAnnotationMatcher = new ExtendedAnnotationMatcher();
	private final ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(extendedAnnotationMatcher);

	@Test
	public void scan_shouldThrowUnsupportedAnnotationException_whenAnnotationToFindIsInUnsupportedPackage() {
		UnsupportedAnnotationException actualException = assertThrows(UnsupportedAnnotationException.class, () -> {
		scanner.scan(ComposedAnnotationScannerTest.class, Retention.class);
		});
		assertEquals(
				actualException.getMessageTemplate().formatted(Retention.class.getName()),
				actualException.getMessage()
		);
	}

	@Test
	public void scanOne_shouldThrowUnsupportedAnnotationException_whenAnnotationToFindIsInUnsupportedPackage() {
		UnsupportedAnnotationException actualException = assertThrows(UnsupportedAnnotationException.class, () -> {
			scanner.scanOne(ComposedAnnotationScannerTest.class, Retention.class);
		});
		assertEquals(
				actualException.getMessageTemplate().formatted(Retention.class.getName()),
				actualException.getMessage()
		);
	}

	@Test
	public void scanOneStrict_shouldThrowUnsupportedAnnotationException_whenAnnotationToFindIsInUnsupportedPackage() {
		UnsupportedAnnotationException actualException = assertThrows(UnsupportedAnnotationException.class, () -> {
			scanner.scanOneStrict(ComposedAnnotationScannerTest.class, Retention.class);
		});
		assertEquals(
				actualException.getMessageTemplate().formatted(Retention.class.getName()),
				actualException.getMessage()
		);
	}

	@TestAnnotationA("A on DirectlyPresent")
	static class DirectlyPresent {}

	@Test
	public void scan_shouldFindDirectlyPresentAnnotation() {
		List<TestAnnotationA> composedAnnotations = scanner.scan(DirectlyPresent.class, TestAnnotationA.class);

		assertEquals(1, composedAnnotations.size());
		assertEquals("A on DirectlyPresent", composedAnnotations.getFirst().value());
	}

	@Test
	public void scanOne_shouldFindDirectlyPresentAnnotation() {
		Optional<TestAnnotationA> composedAnnotation = scanner.scanOne(DirectlyPresent.class, TestAnnotationA.class);

		assertTrue(composedAnnotation.isPresent());
		assertEquals("A on DirectlyPresent", composedAnnotation.get().value());
	}

	@Test
	public void scanOneStrict_shouldFindDirectlyPresentAnnotation() {
		Optional<TestAnnotationA> composedAnnotation = scanner.scanOneStrict(DirectlyPresent.class, TestAnnotationA.class);

		assertTrue(composedAnnotation.isPresent());
		assertEquals("A on DirectlyPresent", composedAnnotation.get().value());
	}

	@TestAnnotationA("A on DirectlyPresentRepeated #1")
	@TestAnnotationA("A on DirectlyPresentRepeated #2")
	static class DirectlyPresentRepeated {}

	@RepeatedTestAnnotationA(value = {
			@TestAnnotationA("A on DirectlyPresentRepeated #1"),
			@TestAnnotationA("A on DirectlyPresentRepeated #2")
	})
	static class DirectlyPresentRepeatedContaining {}

	@ParameterizedTest
	@ValueSource(classes = {DirectlyPresentRepeated.class, DirectlyPresentRepeatedContaining.class})
	public void scan_shouldFindMultipleDirectlyPresentAnnotations(Class<?> testClass) {
		List<TestAnnotationA> composedAnnotations = scanner.scan(testClass, TestAnnotationA.class);

		assertEquals(2, composedAnnotations.size());
		assertTrue(composedAnnotations.stream().anyMatch(a -> a.value().equals("A on DirectlyPresentRepeated #1")));
		assertTrue(composedAnnotations.stream().anyMatch(a -> a.value().equals("A on DirectlyPresentRepeated #2")));
	}

	@ParameterizedTest
	@ValueSource(classes = {DirectlyPresentRepeated.class, DirectlyPresentRepeatedContaining.class})
	public void scanOne_shouldFindMultipleDirectlyPresentAnnotations(Class<?> testClass) {
		Optional<TestAnnotationA> composedAnnotation = scanner.scanOne(testClass, TestAnnotationA.class);

		assertTrue(composedAnnotation.isPresent());
		//no guarantee which one is found first, so that is not checked
	}

	@ParameterizedTest
	@ValueSource(classes = {DirectlyPresentRepeated.class, DirectlyPresentRepeatedContaining.class})
	public void scanOneStrict_shouldThrowException_ifFoundMultipleDirectlyPresentAnnotations(Class<?> testClass) {
		MultipleAnnotationsScannedException exception = assertThrows(MultipleAnnotationsScannedException.class, () -> {
			scanner.scanOneStrict(testClass, TestAnnotationA.class);
		});

		var repeatedAnnotationContainer = DirectlyPresentRepeated.class.getAnnotation(RepeatedTestAnnotationA.class);
		assertEquals(
				exception.getMessageTemplate().formatted(2, Arrays.asList(repeatedAnnotationContainer.value())),
				exception.getMessage()
		);
	}

	//@TestAnnotationB is annotated with @TestAnnotationA, so this class has composed @TestAnnotationA
	@TestAnnotationB("B on ComposedPresentOneLayer")
	static class ComposedPresentOneLayer {}

	@Test
	public void scan_shouldFindComposedAnnotation_whenComposedOneLayer() {
		List<TestAnnotationA> composedAnnotations = scanner.scan(ComposedPresentOneLayer.class, TestAnnotationA.class);

		assertEquals(1, composedAnnotations.size());
		assertEquals("A on B", composedAnnotations.getFirst().value());
	}

	@Test
	public void scanOne_shouldFindComposedAnnotation_whenComposedOneLayer() {
		Optional<TestAnnotationA> composedAnnotation = scanner.scanOne(ComposedPresentOneLayer.class, TestAnnotationA.class);

		assertTrue(composedAnnotation.isPresent());
		assertEquals("A on B", composedAnnotation.get().value());
	}

	@Test
	public void scanOneStrict_shouldFindComposedAnnotation_whenComposedOneLayer() {
		Optional<TestAnnotationA> composedAnnotation = scanner.scanOneStrict(ComposedPresentOneLayer.class, TestAnnotationA.class);

		assertTrue(composedAnnotation.isPresent());
		assertEquals("A on B", composedAnnotation.get().value());
	}

	//@TestAnnotationC is annotated with @TestAnnotationB and @TestAnnotationA
	//@TestAnnotationB is also annotated with @TestAnnotationA
	//so this class has composed @TestAnnotationA, two of them
	@TestAnnotationC("C on ComposedPresentTwoLayers")
	static class ComposedPresentTwoLayers {}

	@Test
	public void scan_shouldFindComposedAnnotation_whenComposedTwoLayers() {
		List<TestAnnotationA> composedAnnotations = scanner.scan(ComposedPresentTwoLayers.class, TestAnnotationA.class);

		assertEquals(2, composedAnnotations.size());
		assertTrue(composedAnnotations.stream().anyMatch(a -> a.value().equals("A on B")));
		assertTrue(composedAnnotations.stream().anyMatch(a -> a.value().equals("A on C")));
	}

	@Test
	public void scanOne_shouldFindComposedAnnotation_whenComposedTwoLayers() {
		Optional<TestAnnotationA> composedAnnotation = scanner.scanOne(ComposedPresentTwoLayers.class, TestAnnotationA.class);

		assertTrue(composedAnnotation.isPresent());
		//no guarantee which one is found first, so that is not checked
	}

	@Test
	public void scanOneStrict_shouldThrowException_whenFoundMultipleAnnotations_composedInTwoLayers() {
		MultipleAnnotationsScannedException exception = assertThrows(MultipleAnnotationsScannedException.class, () -> {
			scanner.scanOneStrict(ComposedPresentTwoLayers.class, TestAnnotationA.class);
		});

		List<TestAnnotationA> composedAnnotationsExpectedInMessage = scanner.scan(ComposedPresentTwoLayers.class, TestAnnotationA.class);
		assertEquals(
				exception.getMessageTemplate().formatted(2, composedAnnotationsExpectedInMessage),
				exception.getMessage()
		);
	}

	//@TestSelfAnnotation is placed on itself
	@TestSelfAnnotation("TestSelfAnnotation on DirectlyPresentSelfAnnotation")
	static class DirectlyPresentSelfAnnotation {}

	@Test
	public void shouldFindComposedAnnotation_whenDirectlyPresentOnClass_andIsSelfAnnotation() {
		List<TestSelfAnnotation> composedAnnotations = scanner.scan(DirectlyPresentSelfAnnotation.class, TestSelfAnnotation.class);

		assertEquals(1, composedAnnotations.size());
		assertEquals("TestSelfAnnotation on DirectlyPresentSelfAnnotation", composedAnnotations.getFirst().value());
	}

	@TestCircularAnnotationB
	static class CircularAnnotations {}

	@Test
	public void shouldFindComposedAnnotation_whenAnnotationIsCircular() {
		List<TestCircularAnnotationA> composedAnnotationsA = scanner.scan(CircularAnnotations.class, TestCircularAnnotationA.class);
		List<TestCircularAnnotationB> composedAnnotationsB = scanner.scan(CircularAnnotations.class, TestCircularAnnotationB.class);

		assertEquals(1, composedAnnotationsA.size());
		assertEquals(1, composedAnnotationsB.size());
	}

}
