package org.tframework.core.elements.assembler;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.source.MethodElementSource;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolverAggregator;

/**
 * An {@link ElementAssembler} that creates instances of method elements (in other words: where methods are
 * marked as elements).
 * <ul>
 *     <li>An instance of the parent element is requested from the parent {@link ElementContext}.</li>
 *     <li>The given {@link MethodElementSource} is used to retrieve the method and parameters.</li>
 *     <li>Dependencies (method parameters) are resolved.</li>
 * </ul>
 * This method is then invoked with the parent instance and the resolved dependencies.
 */
@Slf4j
public class MethodElementAssembler extends ElementAssembler {

    static final String ASSEMBLED_FROM = "Method";
    static final String DEPENDENCY_DECLARED_AS = "Method parameter";

    /**
     * Element context whose instances this assembler assembles.
     */
    private final ElementContext elementContext;
    private final MethodElementSource methodElementSource;
    private final DependencyResolverAggregator dependencyResolverAggregator;

    private MethodElementAssembler(
           ElementContext elementContext,
           DependencyResolverAggregator dependencyResolverAggregator
    ) {
        super(elementContext.getName(), elementContext.getType());
        this.elementContext = elementContext;
        this.methodElementSource = (MethodElementSource) elementContext.getSource();
        this.dependencyResolverAggregator = dependencyResolverAggregator;
    }

    @Override
    public Object assemble(ElementDependencyGraph dependencyGraph) throws ElementAssemblingException {
        try {
            //this is a special dependency between the method element and it's parent
            dependencyGraph.addDependency(elementContext, methodElementSource.parentElementContext());

            Object parentElementInstance = methodElementSource.parentElementContext().requestInstance();
            log.debug("Requested an instance of parent element '{}' for method element '{}'",
                    methodElementSource.parentElementContext().getName(), elementName);

            //this may add additional dependencies to the graph
            Object[] methodArgs = resolveMethodInvocationDependencies(dependencyGraph);
            log.debug("Resolved {} method parameters that will be used to assemble element '{}'",
                    methodArgs.length, elementName);

            Object elementInstance = methodElementSource.method().invoke(parentElementInstance, methodArgs);
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

    private Object[] resolveMethodInvocationDependencies(ElementDependencyGraph dependencyGraph) {
        Object[] methodArgs = new Object[methodElementSource.elementConstructionParameters().size()];
        for (int i = 0; i < methodArgs.length; i++) {
            var methodParameter = methodElementSource.elementConstructionParameters().get(i);
            DependencyDefinition dependencyDefinition = DependencyDefinition.fromParameter(methodParameter);
            methodArgs[i] = dependencyResolverAggregator.resolveDependency(
                    dependencyDefinition, elementContext, dependencyGraph, DEPENDENCY_DECLARED_AS
            );
        }
        return methodArgs;
    }

    @Builder
    static MethodElementAssembler create(
            ElementContext elementContext,
            DependencyResolverAggregator dependencyResolverAggregator
    ) {
        return new MethodElementAssembler(elementContext, dependencyResolverAggregator);
    }
}
