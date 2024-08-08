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

import java.lang.annotation.Retention;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ComposedAnnotationScannerTest {

    /*
    This may not be the best approach, but instead of using a mocks, I decided to
    use a concrete implementation, to significantly reduce mocking noise in these tests.
     */
    private final ExtendedAnnotationMatcher extendedAnnotationMatcher = AnnotationMatchersFactory.createExtendedAnnotationMatcher();
    private final RepeatedAnnotationHandler repeatedAnnotationHandler = RepeatedAnnotationHandlersFactory.create();
    private final ComposedAnnotationScanner scanner = new ComposedAnnotationScanner(extendedAnnotationMatcher, repeatedAnnotationHandler);

    @TestAnnotationA("A on HasManyAnnotations #1")
    @TestAnnotationA("A on HasManyAnnotations #2")
    @TestAnnotationB("B on HasManyAnnotations")
    @TestAnnotationC("C on HasManyAnnotations")
    static class HasManyAnnotations {}

    @Test
    public void scanAll_shouldFindAllSupportedAnnotations() {
        var allAnnotations = scanner.scan(HasManyAnnotations.class);
        //test annotation A should be present 4 times: 2 directly, 1 on B, 2 on C
        assertEquals(5, allAnnotations.stream().filter(a -> a instanceof TestAnnotationA).count());
        //test annotation B should be present 2 times: 1 directly, 1 on C
        assertEquals(2, allAnnotations.stream().filter(a -> a instanceof TestAnnotationB).count());
        //test annotation C should be present 1 time
        assertEquals(1, allAnnotations.stream().filter(a -> a instanceof TestAnnotationC).count());
    }

    @Test
    public void scan_shouldThrowUnsupportedAnnotationException_whenAnnotationToFindIsInUnsupportedPackage() {
        UnsupportedAnnotationException actualException = assertThrows(UnsupportedAnnotationException.class,
                () -> scanner.scan(ComposedAnnotationScannerTest.class, Retention.class));
        assertEquals(
                actualException.getMessageTemplate().formatted(Retention.class.getName()),
                actualException.getMessage()
        );
    }

    @Test
    public void scanOne_shouldThrowUnsupportedAnnotationException_whenAnnotationToFindIsInUnsupportedPackage() {
        UnsupportedAnnotationException actualException = assertThrows(UnsupportedAnnotationException.class,
                () -> scanner.scanOne(ComposedAnnotationScannerTest.class, Retention.class));
        assertEquals(
                actualException.getMessageTemplate().formatted(Retention.class.getName()),
                actualException.getMessage()
        );
    }

    @Test
    public void scanOneStrict_shouldThrowUnsupportedAnnotationException_whenAnnotationToFindIsInUnsupportedPackage() {
        UnsupportedAnnotationException actualException = assertThrows(UnsupportedAnnotationException.class,
                () -> scanner.scanOneStrict(ComposedAnnotationScannerTest.class, Retention.class));
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
        MultipleAnnotationsScannedException exception = assertThrows(MultipleAnnotationsScannedException.class,
                () -> scanner.scanOneStrict(testClass, TestAnnotationA.class));

        var repeatedAnnotationContainer = DirectlyPresentRepeated.class.getAnnotation(RepeatedTestAnnotationA.class);
        assertEquals(
                exception.getMessageTemplate().formatted(testClass, 2, Arrays.asList(repeatedAnnotationContainer.value())),
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
        MultipleAnnotationsScannedException exception = assertThrows(MultipleAnnotationsScannedException.class,
                () -> scanner.scanOneStrict(ComposedPresentTwoLayers.class, TestAnnotationA.class));

        List<TestAnnotationA> composedAnnotationsExpectedInMessage = scanner.scan(ComposedPresentTwoLayers.class, TestAnnotationA.class);
        assertEquals(
                exception.getMessageTemplate().formatted(ComposedPresentTwoLayers.class, 2, composedAnnotationsExpectedInMessage),
                exception.getMessage()
        );
    }

    //@TestSelfAnnotation is placed on itself
    @TestSelfAnnotation("TestSelfAnnotation on DirectlyPresentSelfAnnotation")
    static class DirectlyPresentSelfAnnotation {}

    @Test
    public void shouldNotFindComposedAnnotation_whenDirectlyPresentOnClass_andIsSelfAnnotation() {
        assertThrows(UnsupportedAnnotationException.class,
                () ->scanner.scan(DirectlyPresentSelfAnnotation.class, TestSelfAnnotation.class));
    }

    @Test
    public void shouldNotFindComposedAnnotation_whenNotPresent_butAnotherSelfAnnotationIsPresent() {
        List<TestAnnotationA> composedAnnotations = scanner.scan(DirectlyPresentSelfAnnotation.class, TestAnnotationA.class);

        assertEquals(0, composedAnnotations.size());
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

    @Test
    public void shouldNotFindComposedAnnotation_whenNotPresent_butAnotherCircularAnnotationIsPresent() {
        List<TestAnnotationA> composedAnnotations = scanner.scan(CircularAnnotations.class, TestAnnotationA.class);

        assertEquals(0, composedAnnotations.size());
    }

}
