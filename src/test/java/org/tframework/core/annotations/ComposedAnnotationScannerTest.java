package org.tframework.core.annotations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Retention;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComposedAnnotationScannerTest {

    @Test
    public void shouldThrowUnsupportedAnnotationException_whenAnnotationToFindIsInUnsupportedPackage() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(ComposedAnnotationScannerTest.class);

        UnsupportedAnnotationException actualException = assertThrows(UnsupportedAnnotationException.class, () -> {
           scanner.scanComposedAnnotations(Retention.class);
        });
        assertEquals(
                "Composed annotation scanning is not supported for annotation '%s'".formatted(Retention.class.getName()),
                actualException.getMessage()
        );
    }

    @TestAnnotationA("A on DirectlyPresent")
    static class DirectlyPresent {}

    @Test
    public void shouldFindDirectlyPresentAnnotation() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(DirectlyPresent.class);

        List<TestAnnotationA> composedAnnotations = scanner.scanComposedAnnotations(TestAnnotationA.class);

        assertEquals(1, composedAnnotations.size());
        assertEquals("A on DirectlyPresent", composedAnnotations.getFirst().value());
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
    public void shouldFindMultipleDirectlyPresentAnnotations(Class<?> testClass) {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(testClass);

        List<TestAnnotationA> composedAnnotations = scanner.scanComposedAnnotations(TestAnnotationA.class);

        assertEquals(2, composedAnnotations.size());
        assertTrue(composedAnnotations.stream().anyMatch(a -> a.value().equals("A on DirectlyPresentRepeated #1")));
        assertTrue(composedAnnotations.stream().anyMatch(a -> a.value().equals("A on DirectlyPresentRepeated #2")));
    }

    //@TestAnnotationB is annotated with @TestAnnotationA, so this class has composed @TestAnnotationA
    @TestAnnotationB("B on ComposedPresentOneLayer")
    static class ComposedPresentOneLayer {}

    @Test
    public void shouldFindComposedAnnotation_whenComposedOneLayer() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(ComposedPresentOneLayer.class);

        List<TestAnnotationA> composedAnnotations = scanner.scanComposedAnnotations(TestAnnotationA.class);

        assertEquals(1, composedAnnotations.size());
        assertEquals("A on B", composedAnnotations.getFirst().value());
    }

    //@TestAnnotationC is annotated with @TestAnnotationB and @TestAnnotationA
    //@TestAnnotationB is also annotated with @TestAnnotationA
    //so this class has composed @TestAnnotationA, two of them
    @TestAnnotationC("C on ComposedPresentTwoLayers")
    static class ComposedPresentTwoLayers {}

    @Test
    public void shouldFindComposedAnnotation_whenComposedTwoLayers() {
        ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(ComposedPresentTwoLayers.class);

        List<TestAnnotationA> composedAnnotations = scanner.scanComposedAnnotations(TestAnnotationA.class);

        assertEquals(2, composedAnnotations.size());
        assertTrue(composedAnnotations.stream().anyMatch(a -> a.value().equals("A on B")));
        assertTrue(composedAnnotations.stream().anyMatch(a -> a.value().equals("A on C")));
    }

    //TODO: implement and test annotation scanning if an annotation self annotates itself.
    //TODO: implement and test annotation scanning if annotations are placed on each other circularly.

}