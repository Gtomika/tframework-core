/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.ElementConstructor;
import org.tframework.core.elements.context.source.ClassElementSource;
import org.tframework.core.elements.scanner.ElementScanningResult;
import org.tframework.core.reflection.constructor.ConstructorFiltersFactory;
import org.tframework.core.reflection.constructor.ConstructorScannersFactory;

class ClassElementContextAssemblerTest {

    //maybe mocking the dependencies would be better, but it would require a lot of work
    private final ClassElementContextAssembler assembler = ClassElementContextAssembler.builder()
            .constructorScanner(ConstructorScannersFactory.createDefaultConstructorScanner())
            .constructorFilter(ConstructorFiltersFactory.createDefaultConstructorFilter())
            .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
            .build();

    @Test
    public void shouldThrowException_whenClassHasNoPublicConstructors() {
        var scanningResult = asScanningResult(NoPublicConstructors.class);

        var exception = assertThrows(ElementContextAssemblingException.class, () -> assembler.assemble(scanningResult));

        String expectedMessage = exception.getMessageTemplate().formatted(
                NoPublicConstructors.class.getName(),
                ClassElementContextAssembler.DECLARED_AS,
                NoPublicConstructors.class.getName(),
                ClassElementContextAssembler.NO_PUBLIC_CONSTRUCTORS_ERROR
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowException_whenClassHasMultiplePublicConstructors_butTheyAreNotAnnotated() {
        var scanningResult = asScanningResult(MultiplePublicConstructors.class);

        var exception = assertThrows(ElementContextAssemblingException.class, () -> assembler.assemble(scanningResult));

        String expectedMessage = exception.getMessageTemplate().formatted(
                MultiplePublicConstructors.class.getName(),
                ClassElementContextAssembler.DECLARED_AS,
                MultiplePublicConstructors.class.getName(),
                ClassElementContextAssembler.MULTIPLE_PUBLIC_CONSTRUCTORS_ERROR
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowException_whenClassHasMultipleAnnotatedConstructors() {
        var scanningResult = asScanningResult(ClassWithMultipleAnnotatedConstructors.class);

        var exception = assertThrows(ElementContextAssemblingException.class, () -> assembler.assemble(scanningResult));

        String expectedMessage = exception.getMessageTemplate().formatted(
                ClassWithMultipleAnnotatedConstructors.class.getName(),
                ClassElementContextAssembler.DECLARED_AS,
                ClassWithMultipleAnnotatedConstructors.class.getName(),
                ClassElementContextAssembler.MULTIPLE_ANNOTATED_CONSTRUCTORS_ERROR
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldAssembleElementContext_whenClassIsAValidElement() {
        var scanningResult = asScanningResult(ValidElement.class);

        var elementContext = assembler.assemble(scanningResult);

        assertEquals(ValidElement.class, elementContext.getType());
        if(elementContext.getSource() instanceof ClassElementSource source) {
            assertEquals(1, elementContext.getSource().elementConstructionParameters().size());
        } else {
            fail("Element context source is not a class element source.");
        }
    }

    private ElementScanningResult<Class<?>> asScanningResult(Class<?> clazz) {
        return new ElementScanningResult<>(clazz.getAnnotation(Element.class), clazz);
    }

    @Element
    static class NoPublicConstructors {
        private NoPublicConstructors() {}
    }

    @Element
    static class MultiplePublicConstructors {
        public MultiplePublicConstructors() {}
        public MultiplePublicConstructors(int i) {}
    }

    @Element
    static class ClassWithMultipleAnnotatedConstructors {
        @ElementConstructor
        public ClassWithMultipleAnnotatedConstructors() {}
        @ElementConstructor
        public ClassWithMultipleAnnotatedConstructors(int i) {}
    }

    @Element
    static class ValidElement {
        public ValidElement() {}
        @ElementConstructor
        public ValidElement(Integer i) {}
    }

}
