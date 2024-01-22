/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.annotations.MultipleAnnotationsScannedException;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.annotations.InjectProperty;

@InjectProperty("test") //these exist just to get them as objects in the test
@InjectElement("test")
@ExtendWith(MockitoExtension.class)
class InjectAnnotationScannerTest {

    private String testFieldActual;

    @Mock
    private AnnotationScanner annotationScanner;

    private InjectAnnotationScanner injectAnnotationScanner;
    private Field testField;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        injectAnnotationScanner = new InjectAnnotationScanner(annotationScanner);
        testField = this.getClass().getDeclaredField("testFieldActual");
    }

    @Test
    public void shouldThrowException_ifMultipleInjectAnnotationsAreFound_withTheSameType() {
        when(annotationScanner.scanOneStrict(testField, InjectElement.class))
                .thenThrow(MultipleAnnotationsScannedException.class);

        assertThrows(MultipleAnnotationsScannedException.class, () -> {
            injectAnnotationScanner.findInjectAnnotation(testField, InjectElement.class);
        });

        //no point testing the exception message, because it was provided by a mock
    }

    @Test
    public void shouldThrowException_ifMultipleInjectAnnotationsAreFound_withDifferentTypes() {
        List<? extends Annotation> multipleInjectAnnotations = List.of(
                this.getClass().getAnnotation(InjectElement.class),
                this.getClass().getAnnotation(InjectProperty.class)
        );
        when(annotationScanner.scanOneStrict(testField, InjectElement.class))
                .thenReturn(Optional.of((InjectElement) multipleInjectAnnotations.get(0)));
        when(annotationScanner.scanOneStrict(testField, InjectProperty.class))
                .thenReturn(Optional.of((InjectProperty) multipleInjectAnnotations.get(1)));

        var exception = assertThrows(MultipleAnnotationsScannedException.class, () -> {
            injectAnnotationScanner.findInjectAnnotation(testField, InjectElement.class);
        });

        assertEquals(
                exception.getMessageTemplate().formatted(multipleInjectAnnotations.size(), multipleInjectAnnotations),
                exception.getMessage()
        );
    }

    @ParameterizedTest
    @ValueSource(classes = {InjectElement.class, InjectProperty.class})
    public void shouldReturnEmpty_ifNoInjectAnnotationsAreFound(Class<? extends Annotation> injectAnnotationType) {
        when(annotationScanner.scanOneStrict(testField, InjectElement.class))
                .thenReturn(Optional.empty());
        when(annotationScanner.scanOneStrict(testField, InjectProperty.class))
                .thenReturn(Optional.empty());

        var injectAnnotation = injectAnnotationScanner.findInjectAnnotation(testField, injectAnnotationType);

        assertTrue(injectAnnotation.isEmpty());
    }

    @Test
    public void shouldReturnInjectAnnotation_ifExactlyOneIsFound() {
        InjectElement injectElementAnnotation = this.getClass().getAnnotation(InjectElement.class);
        when(annotationScanner.scanOneStrict(testField, InjectElement.class))
                .thenReturn(Optional.of(injectElementAnnotation));
        when(annotationScanner.scanOneStrict(testField, InjectProperty.class))
                .thenReturn(Optional.empty());

        var injectAnnotation = injectAnnotationScanner.findInjectAnnotation(testField, InjectElement.class);

        assertTrue(injectAnnotation.isPresent());
        assertEquals(injectElementAnnotation, injectAnnotation.get());
    }

}
