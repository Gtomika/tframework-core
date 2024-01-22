package org.tframework.core.elements.dependency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.properties.PropertiesContainer;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DependencyResolversFactoryTest {

    @Mock
    private ElementsContainer elementsContainer;

    @Mock
    private PropertiesContainer propertiesContainer;

    private final DependencyResolutionInput input = new DependencyResolutionInput(elementsContainer, propertiesContainer);

    @Test
    void shouldCreateParameterDependencyResolvers() {
        var resolvers = DependencyResolversFactory.createParameterDependencyResolvers(input);
        assertInstanceOf(ElementDependencyResolver.class, resolvers.get(0));
        assertInstanceOf(PropertyDependencyResolver.class, resolvers.get(1));
        assertInstanceOf(FallbackDependencyResolver.class, resolvers.get(2));
    }

    @Test
    void shouldCreateFieldDependencyResolvers() {
        var resolvers = DependencyResolversFactory.createFieldDependencyResolvers(input);
        assertInstanceOf(ElementDependencyResolver.class, resolvers.get(0));
        assertInstanceOf(PropertyDependencyResolver.class, resolvers.get(1));
        assertTrue(resolvers.stream().noneMatch(r -> r instanceof FallbackDependencyResolver));
    }
}