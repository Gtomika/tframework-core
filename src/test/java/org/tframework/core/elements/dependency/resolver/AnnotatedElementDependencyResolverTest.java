/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.InjectAnnotationScanner;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

@ExtendWith(MockitoExtension.class)
class AnnotatedElementDependencyResolverTest {

    @Mock
    private InjectAnnotationScanner injectAnnotationScanner;

    @Mock
    private ElementContext originalElementContext;

    @Mock
    private ElementContext dependencyElementContext;

    @Mock
    private ElementsContainer dependencySource;

    private AnnotatedElementDependencyResolver elementDependencyResolver;

    private Field someField;
    private InjectElement injectElementWithNameProvided;
    private DependencyDefinition dependencyDefinitionWithNameProvided;

    private Field otherField;
    private InjectElement injectElementWithNameNotProvided;
    private DependencyDefinition dependencyDefinitionWithNameNotProvided;

    @BeforeEach
    void setUp() throws Exception {
        elementDependencyResolver = new AnnotatedElementDependencyResolver(dependencySource, injectAnnotationScanner);

        someField = this.getClass().getDeclaredField("someString");
        injectElementWithNameProvided = someField.getAnnotation(InjectElement.class);
        dependencyDefinitionWithNameProvided = new DependencyDefinition(someField, someField.getType());

        otherField = this.getClass().getDeclaredField("otherString");
        injectElementWithNameNotProvided = otherField.getAnnotation(InjectElement.class);
        dependencyDefinitionWithNameNotProvided = new DependencyDefinition(otherField, otherField.getType());
    }

    @Test
    public void shouldResolveDependency_whenPresentInDependencySource_withNameProvided() {
        var dependencyGraph = ElementDependencyGraph.empty();
        String expectedDependencyValue = "testDependencyValue";

        when(injectAnnotationScanner.findInjectAnnotation(someField, InjectElement.class))
                .thenReturn(Optional.of(injectElementWithNameProvided));
        when(dependencySource.getElementContext(injectElementWithNameProvided.value()))
                .thenReturn(dependencyElementContext);
        when(dependencyElementContext.requestInstance(dependencyGraph))
                .thenReturn(expectedDependencyValue);

        var resolvedDependency = elementDependencyResolver.resolveDependency(
                dependencyDefinitionWithNameProvided,
                originalElementContext,
                dependencyGraph
        );

        assertTrue(dependencyGraph.containsDependency(originalElementContext, dependencyElementContext));
        if(resolvedDependency.isPresent() && resolvedDependency.get() instanceof String resolvedString) {
            assertEquals(expectedDependencyValue, resolvedString);
        } else {
            fail("Resolved dependency is not a String");
        }
    }

    @Test
    public void shouldResolveDependency_whenPresentInDependencySource_withNameNotProvided() {
        var dependencyGraph = ElementDependencyGraph.empty();
        String expectedDependencyValue = "testDependencyValue";

        when(injectAnnotationScanner.findInjectAnnotation(otherField, InjectElement.class))
                .thenReturn(Optional.of(injectElementWithNameNotProvided));
        when(dependencySource.getElementContext(ElementUtils.getElementNameByType(otherField.getType())))
                .thenReturn(dependencyElementContext);
        when(dependencyElementContext.requestInstance(dependencyGraph))
                .thenReturn(expectedDependencyValue);

        var resolvedDependency = elementDependencyResolver.resolveDependency(
                dependencyDefinitionWithNameNotProvided,
                originalElementContext,
                dependencyGraph
        );

        assertTrue(dependencyGraph.containsDependency(originalElementContext, dependencyElementContext));
        if(resolvedDependency.isPresent() && resolvedDependency.get() instanceof String resolvedString) {
            assertEquals(expectedDependencyValue, resolvedString);
        } else {
            fail("Resolved dependency is not a String");
        }
    }

    @Test
    public void shouldNotResolveDependency_whenNotPresentInDependencySource() {
        when(injectAnnotationScanner.findInjectAnnotation(someField, InjectElement.class))
                .thenReturn(Optional.of(injectElementWithNameProvided));
        when(dependencySource.getElementContext(injectElementWithNameProvided.value()))
                .thenThrow(new RuntimeException("Dependency not found"));

        var resolvedDependency = elementDependencyResolver.resolveDependency(
                dependencyDefinitionWithNameProvided,
                originalElementContext,
                ElementDependencyGraph.empty()
        );
        assertTrue(resolvedDependency.isEmpty());
    }

    @Test
    public void shouldThrowException_whenDependencyResolutionFails() {
        when(injectAnnotationScanner.findInjectAnnotation(someField, InjectElement.class))
                .thenThrow(new RuntimeException("Illegal, multiple inject annotations found"));

        assertThrows(RuntimeException.class, () -> {
            elementDependencyResolver.resolveDependency(
                    dependencyDefinitionWithNameProvided,
                    originalElementContext,
                    ElementDependencyGraph.empty()
            );
        });
    }

    @InjectElement("testDependency")
    private String someString;

    @InjectElement // no name provided
    private String otherString;
}
