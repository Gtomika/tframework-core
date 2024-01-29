package org.tframework.core.elements.assembler;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.source.ClassElementSource;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolverAggregator;

/**
 * An {@link ElementAssembler} that creates instances of class elements (in other words: where classes are
 * marked as elements). The given {@link ClassElementSource} is used to retrieve the constructor and parameters.
 * This constructor is then invoked with the resolved dependencies.
 */
@Slf4j
public class ClassElementAssembler extends ElementAssembler {

    static final String ASSEMBLED_FROM = "Constructor";
    static final String DEPENDENCY_DECLARED_AS = "Constructor parameter";

    /**
     * Element context whose instances this assembler assembles.
     */
    private final ElementContext elementContext;
    private final ClassElementSource classElementSource;
    private final DependencyResolverAggregator aggregator;

    private ClassElementAssembler(ElementContext elementContext, DependencyResolverAggregator aggregator) {
        super(elementContext.getName(), elementContext.getType());
        this.elementContext = elementContext;
        this.classElementSource = (ClassElementSource) elementContext.getSource();
        this.aggregator = aggregator;
    }

    @Override
    public Object assemble(ElementDependencyGraph dependencyGraph) {
        try {
            Object[] constructorArgs = resolveConstructionTimeDependencies(dependencyGraph);
            log.debug("Resolved {} constructor parameters that will be used to assemble element '{}'",
                    constructorArgs.length, elementName);
            Object elementInstance = classElementSource.constructor().newInstance(constructorArgs);
            log.debug("Successfully assembled element '{}': {}", elementName, elementInstance);
            return elementInstance;
        } catch (Exception e) {
            throw ElementAssemblingException.builder()
                    .elementName(elementName)
                    .elementType(elementType)
                    .assemblerClass(this.getClass())
                    .assembledFrom(ASSEMBLED_FROM)
                    .cause(e)
                    .build();
        }
    }

    private Object[] resolveConstructionTimeDependencies(ElementDependencyGraph dependencyGraph) {
        Object[] constructorArgs = new Object[classElementSource.elementConstructionParameters().size()];
        for (int i = 0; i < constructorArgs.length; i++) {
            var constructorParameter = classElementSource.elementConstructionParameters().get(i);
            DependencyDefinition dependencyDefinition = DependencyDefinition.fromParameter(constructorParameter);
            constructorArgs[i] = aggregator.resolveDependency(
                    dependencyDefinition, elementContext, dependencyGraph, DEPENDENCY_DECLARED_AS
            );
        }
        return constructorArgs;
    }

    @Builder
    static ClassElementAssembler from(
            ElementContext elementContext,
            DependencyResolverAggregator dependencyResolverAggregator
    ) {
        return new ClassElementAssembler(elementContext, dependencyResolverAggregator);
    }

}
