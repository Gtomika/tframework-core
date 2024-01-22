package org.tframework.core.elements.assembler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.elements.context.source.ClassElementSource;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.context.source.MethodElementSource;
import org.tframework.core.elements.dependency.DependencyResolutionInput;
import org.tframework.core.elements.dependency.DependencyResolver;
import org.tframework.core.elements.dependency.DependencyResolversFactory;

/**
 * Utilities to create {@link ElementAssembler}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementAssemblersFactory {

    /**
     * Creates a {@link ElementAssembler} that can assemble the given element.
     * @param elementName The name of the element to assemble.
     * @param elementType The type of the element to assemble.
     * @param source The {@link ElementSource} of the element to assemble.
     * @param dependencyResolutionInput The {@link DependencyResolutionInput} to use to
     *                                  create appropriate {@link DependencyResolver}s.
     */
    public static ElementAssembler createElementAssembler(
            String elementName,
            Class<?> elementType,
            ElementSource source,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        return switch (source) {
            case ClassElementSource ces -> createClassElementAssembler(elementName, elementType, ces, dependencyResolutionInput);
            case MethodElementSource mes -> createMethodElementAssembler(elementName, elementType, mes, dependencyResolutionInput);
            default -> throw new IllegalArgumentException("Unexpected element source: " + source);
        };
    }

    private static ClassElementAssembler createClassElementAssembler(
            String elementName,
            Class<?> elementType,
            ClassElementSource source,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        var resolvers = DependencyResolversFactory.createParameterDependencyResolvers(dependencyResolutionInput);
        return ClassElementAssembler.builder()
                .elementName(elementName)
                .elementType(elementType)
                .classElementSource(source)
                .dependencyResolvers(resolvers)
                .build();
    }

    private static MethodElementAssembler createMethodElementAssembler(
            String elementName,
            Class<?> elementType,
            MethodElementSource source,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        var resolvers = DependencyResolversFactory.createParameterDependencyResolvers(dependencyResolutionInput);
        return MethodElementAssembler.builder()
                .elementName(elementName)
                .elementType(elementType)
                .methodElementSource(source)
                .dependencyResolvers(resolvers)
                .build();
    }

}
