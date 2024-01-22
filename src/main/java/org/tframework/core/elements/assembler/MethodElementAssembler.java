package org.tframework.core.elements.assembler;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.context.source.MethodElementSource;
import org.tframework.core.elements.dependency.DependencyResolver;

import java.util.List;

/**
 * An {@link ElementAssembler} that creates instances of method elements (in other words: where methods are
 * marked as elements). The given {@link MethodElementSource} is used to retrieve the method and parameters.
 * This method is then invoked with the resolved dependencies.
 */
@Slf4j
public class MethodElementAssembler extends ElementAssembler {

    private final MethodElementSource methodElementSource;
    private final List<DependencyResolver> dependencyResolvers;

    private MethodElementAssembler(
            String elementName,
            Class<?> elementType,
            MethodElementSource methodElementSource,
            List<DependencyResolver> dependencyResolvers
    ) {
        super(elementName, elementType);
        this.methodElementSource = methodElementSource;
        this.dependencyResolvers = dependencyResolvers;
    }

    @Override
    public Object assemble() throws ElementAssemblingException {
        return null; //TODO
    }

    @Builder
    static MethodElementAssembler create(
            String elementName,
            Class<?> elementType,
            MethodElementSource methodElementSource,
            List<DependencyResolver> dependencyResolvers
    ) {
        return new MethodElementAssembler(elementName, elementType, methodElementSource, dependencyResolvers);
    }
}
