package org.tframework.core.annotations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Retention;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ComposedAnnotationScannerTest {

    @Test
    public void scan_shouldThrowUnsupportedAnnotationException_whenAnnotationToFindIsInUnsupportedPackage() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(ComposedAnnotationScannerTest.class);

        UnsupportedAnnotationException actualException = assertThrows(UnsupportedAnnotationException.class, () -> {
           scanner.scan(Retention.class);
        });
        assertEquals(
                "Composed annotation scanning is not supported for annotation '%s'".formatted(Retention.class.getName()),
                actualException.getMessage()
        );
    }

    @Test
    public void scanOne_shouldThrowUnsupportedAnnotationException_whenAnnotationToFindIsInUnsupportedPackage() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(ComposedAnnotationScannerTest.class);

        UnsupportedAnnotationException actualException = assertThrows(UnsupportedAnnotationException.class, () -> {
            scanner.scanOne(Retention.class);
        });
        assertEquals(
                "Composed annotation scanning is not supported for annotation '%s'".formatted(Retention.class.getName()),
                actualException.getMessage()
        );
    }

    @Test
    public void scanOneStrict_shouldThrowUnsupportedAnnotationException_whenAnnotationToFindIsInUnsupportedPackage() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(ComposedAnnotationScannerTest.class);

        UnsupportedAnnotationException actualException = assertThrows(UnsupportedAnnotationException.class, () -> {
            scanner.scanOneStrict(Retention.class);
        });
        assertEquals(
                "Composed annotation scanning is not supported for annotation '%s'".formatted(Retention.class.getName()),
                actualException.getMessage()
        );
    }

    @TestAnnotationA("A on DirectlyPresent")
    static class DirectlyPresent {}

    @Test
    public void scan_shouldFindDirectlyPresentAnnotation() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(DirectlyPresent.class);

        List<TestAnnotationA> composedAnnotations = scanner.scan(TestAnnotationA.class);

        assertEquals(1, composedAnnotations.size());
        assertEquals("A on DirectlyPresent", composedAnnotations.getFirst().value());
    }

    @Test
    public void scanOne_shouldFindDirectlyPresentAnnotation() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(DirectlyPresent.class);

        Optional<TestAnnotationA> composedAnnotation = scanner.scanOne(TestAnnotationA.class);

        assertTrue(composedAnnotation.isPresent());
        assertEquals("A on DirectlyPresent", composedAnnotation.get().value());
    }

    @Test
    public void scanOneStrict_shouldFindDirectlyPresentAnnotation() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(DirectlyPresent.class);

        Optional<TestAnnotationA> composedAnnotation = scanner.scanOneStrict(TestAnnotationA.class);

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
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(testClass);

        List<TestAnnotationA> composedAnnotations = scanner.scan(TestAnnotationA.class);

        assertEquals(2, composedAnnotations.size());
        assertTrue(composedAnnotations.stream().anyMatch(a -> a.value().equals("A on DirectlyPresentRepeated #1")));
        assertTrue(composedAnnotations.stream().anyMatch(a -> a.value().equals("A on DirectlyPresentRepeated #2")));
    }

    @ParameterizedTest
    @ValueSource(classes = {DirectlyPresentRepeated.class, DirectlyPresentRepeatedContaining.class})
    public void scanOne_shouldFindMultipleDirectlyPresentAnnotations(Class<?> testClass) {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(testClass);

        Optional<TestAnnotationA> composedAnnotation = scanner.scanOne(TestAnnotationA.class);

        assertTrue(composedAnnotation.isPresent());
        //no guarantee which one is found first, so that is not checked
    }

    @ParameterizedTest
    @ValueSource(classes = {DirectlyPresentRepeated.class, DirectlyPresentRepeatedContaining.class})
    public void scanOneStrict_shouldThrowException_ifFoundMultipleDirectlyPresentAnnotations(Class<?> testClass) {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(testClass);

        MultipleAnnotationsScannedException exception = assertThrows(MultipleAnnotationsScannedException.class, () -> {
            scanner.scanOneStrict(TestAnnotationA.class);
        });

        var repeatedAnnotationContainer = DirectlyPresentRepeated.class.getAnnotation(RepeatedTestAnnotationA.class);
        assertEquals(
                "At most 1 annotation was allowed, but found %d: %s".formatted(2, Arrays.asList(repeatedAnnotationContainer.value())),
                exception.getMessage()
        );
    }

    //@TestAnnotationB is annotated with @TestAnnotationA, so this class has composed @TestAnnotationA
    @TestAnnotationB("B on ComposedPresentOneLayer")
    static class ComposedPresentOneLayer {}

    @Test
    public void scan_shouldFindComposedAnnotation_whenComposedOneLayer() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(ComposedPresentOneLayer.class);

        List<TestAnnotationA> composedAnnotations = scanner.scan(TestAnnotationA.class);

        assertEquals(1, composedAnnotations.size());
        assertEquals("A on B", composedAnnotations.getFirst().value());
    }

    @Test
    public void scanOne_shouldFindComposedAnnotation_whenComposedOneLayer() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(ComposedPresentOneLayer.class);

        Optional<TestAnnotationA> composedAnnotation = scanner.scanOne(TestAnnotationA.class);

        assertTrue(composedAnnotation.isPresent());
        assertEquals("A on B", composedAnnotation.get().value());
    }

    @Test
    public void scanOneStrict_shouldFindComposedAnnotation_whenComposedOneLayer() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(ComposedPresentOneLayer.class);

        Optional<TestAnnotationA> composedAnnotation = scanner.scanOneStrict(TestAnnotationA.class);

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
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(ComposedPresentTwoLayers.class);

        List<TestAnnotationA> composedAnnotations = scanner.scan(TestAnnotationA.class);

        assertEquals(2, composedAnnotations.size());
        assertTrue(composedAnnotations.stream().anyMatch(a -> a.value().equals("A on B")));
        assertTrue(composedAnnotations.stream().anyMatch(a -> a.value().equals("A on C")));
    }

    @Test
    public void scanOne_shouldFindComposedAnnotation_whenComposedTwoLayers() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(ComposedPresentTwoLayers.class);

        Optional<TestAnnotationA> composedAnnotation = scanner.scanOne(TestAnnotationA.class);

        assertTrue(composedAnnotation.isPresent());
        //no guarantee which one is found first, so that is not checked
    }

    @Test
    public void scanOneStrict_shouldThrowException_whenFoundMultipleAnnotations_composedInTwoLayers() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(ComposedPresentTwoLayers.class);

        MultipleAnnotationsScannedException exception = assertThrows(MultipleAnnotationsScannedException.class, () -> {
            scanner.scanOneStrict(TestAnnotationA.class);
        });

        List<TestAnnotationA> composedAnnotationsExpectedInMessage = scanner.scan(TestAnnotationA.class);
        assertEquals(
                "At most 1 annotation was allowed, but found %d: %s".formatted(2, composedAnnotationsExpectedInMessage),
                exception.getMessage()
        );
    }

    //@TestSelfAnnotation is placed on itself
    @TestSelfAnnotation("TestSelfAnnotation on DirectlyPresentSelfAnnotation")
    static class DirectlyPresentSelfAnnotation {}

    @Test
    public void shouldFindComposedAnnotation_whenDirectlyPresentOnClass_andIsSelfAnnotation() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(DirectlyPresentSelfAnnotation.class);

        List<TestSelfAnnotation> composedAnnotations = scanner.scan(TestSelfAnnotation.class);

        assertEquals(1, composedAnnotations.size());
        assertEquals("TestSelfAnnotation on DirectlyPresentSelfAnnotation", composedAnnotations.getFirst().value());
    }

    @TestCircularAnnotationB
    static class CircularAnnotations {}

    @Test
    public void shouldFindComposedAnnotation_whenAnnotationIsCircular() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(CircularAnnotations.class);

        List<TestCircularAnnotationA> composedAnnotationsA = scanner.scan(TestCircularAnnotationA.class);
        List<TestCircularAnnotationB> composedAnnotationsB = scanner.scan(TestCircularAnnotationB.class);

        assertEquals(1, composedAnnotationsA.size());
        assertEquals(1, composedAnnotationsB.size());
    }

}