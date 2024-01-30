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
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolverAggregator;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MethodElementAssemblerTest {

    @Mock
    private ElementContext elementContext;

    @Mock
    private ElementContext parentElementContext;

    @Mock
    private DependencyResolverAggregator aggregator;

    private MethodElementAssembler methodElementAssembler;
    private DependencyDefinition dummyStringDependencyDefinition;

    @BeforeEach
    void setUp() throws Exception {
        Method method = MethodElementAssemblerTest.class.getDeclaredMethod("createDummyElement", String.class);
        var methodElementSource = new MethodElementSource(method, parentElementContext);

        when(elementContext.getName()).thenReturn("dummyElement");
        doReturn(DummyElement.class).when(elementContext).getType();
        when(elementContext.getSource()).thenReturn(methodElementSource);

        methodElementAssembler = MethodElementAssembler.builder()
                .elementContext(elementContext)
                .dependencyResolverAggregator(aggregator)
                .build();
        dummyStringDependencyDefinition = DependencyDefinition.fromParameter(method.getParameters()[0]);

        when(parentElementContext.getName()).thenReturn("parentElement");
        when(parentElementContext.requestInstance()).thenReturn(new MethodElementAssemblerTest());
    }

    @Test
    public void shouldAssembleElementFromMethod() {
        var dependencyGraph = ElementDependencyGraph.empty();
        DummyElement expectedElement = new DummyElement("dummyString");

        when(aggregator.resolveDependency(
                dummyStringDependencyDefinition,
                elementContext,
                dependencyGraph,
                MethodElementAssembler.DEPENDENCY_DECLARED_AS
        )).thenReturn(expectedElement.dummyString);

        DummyElement actualElement = (DummyElement) methodElementAssembler.assemble(dependencyGraph);

        assertTrue(dependencyGraph.containsDependency(elementContext, parentElementContext));
        assertEquals(expectedElement, actualElement);
    }

    @Test
    public void shouldThrowException_whenDependencyCannotBeResolved() {
        var dependencyGraph = ElementDependencyGraph.empty();

        when(aggregator.resolveDependency(
                dummyStringDependencyDefinition,
                elementContext,
                dependencyGraph,
                MethodElementAssembler.DEPENDENCY_DECLARED_AS
        )).thenThrow(new RuntimeException("Failed to resolve dependency :("));

        var exception = assertThrows(ElementAssemblingException.class, () -> {
            methodElementAssembler.assemble(dependencyGraph);
        });

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