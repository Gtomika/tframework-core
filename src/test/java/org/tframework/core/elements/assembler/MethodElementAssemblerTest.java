package org.tframework.core.elements.assembler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.source.MethodElementSource;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.DependencyResolver;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MethodElementAssemblerTest {

    @Mock
    private ElementContext parentElementContext;

    @Mock
    private DependencyResolver dependencyResolver;

    private MethodElementSource methodElementSource;
    private MethodElementAssembler methodElementAssembler;
    private DependencyDefinition dummyStringDependencyDefinition;

    @BeforeEach
    void setUp() throws Exception {
        Method method = MethodElementAssemblerTest.class.getDeclaredMethod("createDummyElement", String.class);
        methodElementSource = new MethodElementSource(method, parentElementContext);
        methodElementAssembler = MethodElementAssembler.builder()
                .elementName("dummyElement")
                .elementType(DummyElement.class)
                .methodElementSource(methodElementSource)
                .dependencyResolvers(List.of(dependencyResolver))
                .build();
        dummyStringDependencyDefinition = DependencyDefinition.fromParameter(method.getParameters()[0]);

        when(parentElementContext.getName()).thenReturn("parentElement");
        when(parentElementContext.requestInstance()).thenReturn(new MethodElementAssemblerTest());
    }

    @Test
    public void shouldAssembleElementFromMethod() {
        DummyElement expectedElement = new DummyElement("dummyString");
        when(dependencyResolver.resolveDependency(dummyStringDependencyDefinition))
                .thenReturn(Optional.of(expectedElement.dummyString));

        DummyElement actualElement = (DummyElement) methodElementAssembler.assemble();
        assertEquals(expectedElement, actualElement);
    }

    @Test
    public void shouldThrowException_whenDependencyCannotBeResolved() {
        when(dependencyResolver.resolveDependency(dummyStringDependencyDefinition))
                .thenReturn(Optional.empty());

        var exception = assertThrows(ElementAssemblingException.class, () -> methodElementAssembler.assemble());

        String expectedMessage = exception.getMessageTemplate().formatted(
                "dummyElement",
                DummyElement.class.getName(),
                MethodElementAssembler.class.getName(),
                MethodElementAssembler.ASSEMBLED_FROM
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    record DummyElement(String dummyString) {}

    @Element
    public DummyElement createDummyElement(String dummyString) {
        return new DummyElement(dummyString);
    }

}