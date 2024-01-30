/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.ElementConstructor;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.source.ClassElementSource;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolverAggregator;

@ExtendWith(MockitoExtension.class)
class ClassElementAssemblerTest {

    @Mock
    private DependencyResolverAggregator aggregator;

    @Mock
    private ElementContext elementContext;

    private ClassElementAssembler classElementAssembler;
    private DependencyDefinition dummyStringDependencyDefinition;

    @BeforeEach
    void setUp() throws Exception {
        var constructor = DummyElement.class.getConstructor(String.class);
        var classElementSource = new ClassElementSource(constructor);

        when(elementContext.getName()).thenReturn("dummyElement");
        doReturn(DummyElement.class).when(elementContext).getType();
        when(elementContext.getSource()).thenReturn(classElementSource);

        classElementAssembler = ClassElementAssembler.builder()
                .elementContext(elementContext)
                .dependencyResolverAggregator(aggregator)
                .build();

        dummyStringDependencyDefinition = DependencyDefinition.fromParameter(constructor.getParameters()[0]);
    }

    @Test
    public void shouldAssembleElementFromClass() {
        var dependencyGraph = ElementDependencyGraph.empty();
        DummyElement expectedElement = new DummyElement("dummyString");

        when(aggregator.resolveDependency(
                dummyStringDependencyDefinition,
                elementContext,
                dependencyGraph,
                ClassElementAssembler.DEPENDENCY_DECLARED_AS
        )).thenReturn(expectedElement.dummyString);

        DummyElement actualElement = (DummyElement) classElementAssembler.assemble(dependencyGraph);
        assertEquals(expectedElement, actualElement);
    }

    @Test
    public void shouldThrowException_whenAssemblingFromClass_whenNoDependencyResolversCouldResolveDependency() {
        var dependencyGraph = ElementDependencyGraph.empty();

        when(aggregator.resolveDependency(
                dummyStringDependencyDefinition,
                elementContext,
                dependencyGraph,
                ClassElementAssembler.DEPENDENCY_DECLARED_AS
        )).thenThrow(new RuntimeException("Oof, failed to resolve dependency"));

        var exception = assertThrows(ElementAssemblingException.class, () -> {
            classElementAssembler.assemble(dependencyGraph);
        });

        String expectedMessage = exception.getMessageTemplate().formatted(
                "dummyElement",
                DummyElement.class.getName(),
                ClassElementAssembler.class.getName(),
                ClassElementAssembler.ASSEMBLED_FROM
        );
        assertEquals(expectedMessage, exception.getMessage());
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
