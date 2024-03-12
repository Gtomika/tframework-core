/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.ElementConstructor;
import org.tframework.core.elements.context.source.ClassElementSource;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;
import org.tframework.core.elements.scanner.ElementScanningResult;
import org.tframework.core.properties.PropertiesContainerFactory;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.constructor.ConstructorFilter;
import org.tframework.core.reflection.constructor.ConstructorScanner;

@ExtendWith(MockitoExtension.class)
class ClassElementContextAssemblerTest {

    private final DependencyResolutionInput dependencyResolutionInput = new DependencyResolutionInput(
            ElementsContainer.empty(), PropertiesContainerFactory.empty()
    );

    @Mock
    private ConstructorScanner constructorScanner;

    @Mock
    private ConstructorFilter constructorFilter;

    @Mock
    private AnnotationScanner annotationScanner;

    private ClassElementContextAssembler assembler;

    @BeforeEach
    void setUp() {
        assembler = ClassElementContextAssembler.builder()
                .constructorFilter(constructorFilter)
                .constructorScanner(constructorScanner)
                .annotationScanner(annotationScanner)
                .build();
    }

    @Test
    public void shouldThrowException_whenClassIsNotInstantiable() {
        var scanningResult = asScanningResult(NotInstantiable.class);

        var exception = assertThrows(ElementContextAssemblingException.class, () -> {
            assembler.assemble(scanningResult, dependencyResolutionInput);
        });

        String expectedMessage = exception.getMessageTemplate().formatted(
                NotInstantiable.class.getName(),
                ClassElementContextAssembler.DECLARED_AS,
                NotInstantiable.class.getName(),
                ClassElementContextAssembler.NOT_INSTANTIABLE_ERROR
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowException_whenClassHasNoPublicConstructors() {
        var scanningResult = asScanningResult(DummyElement.class);
        when(constructorScanner.getAllConstructors(DummyElement.class)).thenReturn(Set.of());
        when(constructorFilter.filterPublicConstructors(Set.of())).thenReturn(Set.of());

        var exception = assertThrows(ElementContextAssemblingException.class, () -> {
            assembler.assemble(scanningResult, dependencyResolutionInput);
        });

        String expectedMessage = exception.getMessageTemplate().formatted(
                DummyElement.class.getName(),
                ClassElementContextAssembler.DECLARED_AS,
                DummyElement.class.getName(),
                ClassElementContextAssembler.NO_PUBLIC_CONSTRUCTORS_ERROR
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowException_whenClassHasMultiplePublicConstructors_butTheyAreNotAnnotated() throws Exception {
        var scanningResult = asScanningResult(DummyElement.class);

        var constructor1 = DummyElement.class.getConstructor();
        var constructor2 = DummyElement.class.getConstructor(int.class);
        Set<Constructor<?>> publicConstructors = Set.of(constructor1, constructor2);
        doReturn(publicConstructors).when(constructorScanner).getAllConstructors(DummyElement.class);
        doReturn(publicConstructors).when(constructorFilter).filterPublicConstructors(publicConstructors);
        doReturn(asFilteringResult(Set.of())).when(constructorFilter).filterByAnnotation(
                publicConstructors,
                ElementConstructor.class,
                annotationScanner,
                false
        );

        var exception = assertThrows(ElementContextAssemblingException.class, () -> {
            assembler.assemble(scanningResult, dependencyResolutionInput);
        });

        String expectedMessage = exception.getMessageTemplate().formatted(
                DummyElement.class.getName(),
                ClassElementContextAssembler.DECLARED_AS,
                DummyElement.class.getName(),
                ClassElementContextAssembler.MULTIPLE_PUBLIC_CONSTRUCTORS_ERROR
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowException_whenClassHasMultipleAnnotatedConstructors() throws Exception {
        var scanningResult = asScanningResult(DummyElement.class);

        var constructor1 = DummyElement.class.getConstructor();
        var constructor2 = DummyElement.class.getConstructor(int.class);
        Set<Constructor<?>> publicConstructors = Set.of(constructor1, constructor2);
        doReturn(publicConstructors).when(constructorScanner).getAllConstructors(DummyElement.class);
        doReturn(publicConstructors).when(constructorFilter).filterPublicConstructors(publicConstructors);
        doReturn(asFilteringResult(publicConstructors)).when(constructorFilter).filterByAnnotation(
                publicConstructors,
                ElementConstructor.class,
                annotationScanner,
                false
        );

        var exception = assertThrows(ElementContextAssemblingException.class, () -> {
            assembler.assemble(scanningResult, dependencyResolutionInput);
        });

        String expectedMessage = exception.getMessageTemplate().formatted(
                DummyElement.class.getName(),
                ClassElementContextAssembler.DECLARED_AS,
                DummyElement.class.getName(),
                ClassElementContextAssembler.MULTIPLE_ANNOTATED_CONSTRUCTORS_ERROR
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldAssembleElementContext_whenClassIsAValidElement() throws Exception {
        var scanningResult = asScanningResult(DummyElement.class);

        var constructor1 = DummyElement.class.getConstructor();
        var constructor2 = DummyElement.class.getConstructor(int.class);
        Set<Constructor<?>> publicConstructors = Set.of(constructor1, constructor2);
        doReturn(publicConstructors).when(constructorScanner).getAllConstructors(DummyElement.class);
        doReturn(publicConstructors).when(constructorFilter).filterPublicConstructors(publicConstructors);
        doReturn(asFilteringResult(Set.of(constructor1))).when(constructorFilter).filterByAnnotation(
                publicConstructors,
                ElementConstructor.class,
                annotationScanner,
                false
        );

        var elementContext = assembler.assemble(scanningResult, dependencyResolutionInput);

        assertEquals(DummyElement.class, elementContext.getType());
        if(elementContext.getSource() instanceof ClassElementSource source) {
            //the no-args constructor was mocked to be found
            assertEquals(0, elementContext.getSource().elementConstructionParameters().size());
        } else {
            fail("Element context source is not a class element source.");
        }
    }

    private ElementScanningResult<Class<?>> asScanningResult(Class<?> clazz) {
        return new ElementScanningResult<>(clazz.getAnnotation(Element.class), clazz);
    }

    private Set<AnnotationFilteringResult<ElementConstructor, Constructor<?>>> asFilteringResult(
            Set<Constructor<?>> constructors
    ) throws Exception {
        var injectAnnotation = DummyElement.class.getConstructor().getAnnotation(ElementConstructor.class);
        return constructors.stream()
                .map(c -> new AnnotationFilteringResult<ElementConstructor, Constructor<?>>(injectAnnotation, c))
                .collect(Collectors.toSet());
    }

    @Element
    interface NotInstantiable {}

    @Element
    static class DummyElement {

        @ElementConstructor
        public DummyElement() {}
        public DummyElement(int i) {}

    }

}
