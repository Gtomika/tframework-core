/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.assembler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.source.ClassElementSource;
import org.tframework.core.elements.context.source.MethodElementSource;
import org.tframework.core.elements.dependency.resolver.BasicDependencyResolver;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;
import org.tframework.core.elements.dependency.resolver.DependencyResolverAggregator;
import org.tframework.core.elements.dependency.resolver.DependencyResolversFactory;

/**
 * Utilities to create {@link ElementAssembler}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementAssemblersFactory {

    /**
     * Creates a {@link ElementAssembler} that can assemble the given element.
     * @param elementContext {@link ElementContext} whose instances this assembler assembles.
     * @param dependencyResolutionInput The {@link DependencyResolutionInput} to use to
     *                                  create appropriate {@link BasicDependencyResolver}s.
     */
    public static ElementAssembler createElementAssembler(
            ElementContext elementContext,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        var resolvers = DependencyResolversFactory.createParameterDependencyResolvers(dependencyResolutionInput);
        var aggregator = DependencyResolverAggregator.usingResolvers(resolvers);
        return switch (elementContext.getSource()) {
            case ClassElementSource ces -> createClassElementAssembler(elementContext, aggregator);
            case MethodElementSource mes -> createMethodElementAssembler(elementContext, aggregator);
            default -> throw new IllegalArgumentException("Unexpected element source: " + elementContext.getSource());
        };
    }

    private static ClassElementAssembler createClassElementAssembler(
            ElementContext elementContext, DependencyResolverAggregator aggregator
    ) {
        return ClassElementAssembler.builder()
                .elementContext(elementContext)
                .dependencyResolverAggregator(aggregator)
                .build();
    }

    private static MethodElementAssembler createMethodElementAssembler(
            ElementContext elementContext,
            DependencyResolverAggregator aggregator
    ) {
        return MethodElementAssembler.builder()
                .elementContext(elementContext)
                .dependencyResolverAggregator(aggregator)
                .build();
    }

}
