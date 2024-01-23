package org.tframework.core.elements.assembler;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.ElementConstructor;
import org.tframework.core.elements.context.source.ClassElementSource;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.DependencyResolutionException;
import org.tframework.core.elements.dependency.DependencyResolver;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassElementAssemblerTest {

    @Mock
    private DependencyResolver dependencyResolver;

    private ClassElementSource classElementSource;
    private ClassElementAssembler classElementAssembler;
    private DependencyDefinition dummyStringDependencyDefinition;

    @BeforeEach
    void setUp() throws Exception {
        var constructor = DummyElement.class.getConstructor(String.class);
        classElementSource = new ClassElementSource(constructor);

        classElementAssembler = ClassElementAssembler.builder()
                .elementName("dummyElement")
                .elementType(DummyElement.class)
                .classElementSource(classElementSource)
                .dependencyResolvers(List.of(dependencyResolver))
                .build();

        dummyStringDependencyDefinition = DependencyDefinition.fromParameter(constructor.getParameters()[0]);
    }

    @Test
    public void shouldAssembleElementFromClass() {
        DummyElement expectedElement = new DummyElement("dummyString");
        when(dependencyResolver.resolveDependency(dummyStringDependencyDefinition))
                .thenReturn(Optional.of(expectedElement.dummyString));

        DummyElement actualElement = (DummyElement) classElementAssembler.assemble();
        assertEquals(expectedElement, actualElement);
    }

    @Test
    public void shouldThrowException_whenAssemblingFromClass_whenNoDependencyResolversCouldResolveDependency() {
        when(dependencyResolver.resolveDependency(dummyStringDependencyDefinition))
                .thenReturn(Optional.empty());

        var exception = assertThrows(ElementAssemblingException.class, classElementAssembler::assemble);

        String expectedMessage = exception.getMessageTemplate().formatted(
                "dummyElement",
                DummyElement.class.getName(),
                ClassElementAssembler.class.getName(),
                ClassElementAssembler.ASSEMBLED_FROM
        );
        assertEquals(expectedMessage, exception.getMessage());

        var cause = exception.getCause();
        assertInstanceOf(DependencyResolutionException.class, cause);
    }

    @Element(name = "dummyElement")
    @ToString
    @EqualsAndHashCode
    static class DummyElement {

        private String dummyString;

        @ElementConstructor
        public DummyElement(String dummyString) {
            this.dummyString = dummyString;
        }

    }
}