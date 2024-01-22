package org.tframework.core.elements.assembler;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.context.source.ClassElementSource;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.DependencyResolver;
import org.tframework.core.elements.dependency.DependencyUtils;

import java.util.List;

/**
 * An {@link ElementAssembler} that creates instances of class elements (in other words: where classes are
 * marked as elements). The given {@link ClassElementSource} is used to retrieve the constructor and parameters.
 * This constructor is then invoked with the resolved dependencies.
 */
@Slf4j //TODO cover with unit test
public class ClassElementAssembler extends ElementAssembler {

    static final String ASSEMBLED_FROM = "Constructor";
    static final String DEPENDENCY_DECLARED_AS = "Constructor parameter";

    private final ClassElementSource classElementSource;
    private final List<DependencyResolver> dependencyResolvers;

    private ClassElementAssembler(
            String elementName,
            Class<?> elementType,
            ClassElementSource classElementSource,
            List<DependencyResolver> dependencyResolvers
    ) {
        super(elementName, elementType);
        this.classElementSource = classElementSource;
        this.dependencyResolvers = dependencyResolvers;
    }

    @Override
    public Object assemble() {
        try {
            Object[] constructorArgs = resolveConstructionTimeDependencies();
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

    private Object[] resolveConstructionTimeDependencies() {
        Object[] constructorArgs = new Object[classElementSource.elementConstructionParameters().size()];
        for (int i = 0; i < constructorArgs.length; i++) {
            var constructorParameter = classElementSource.elementConstructionParameters().get(i);
            DependencyDefinition dependencyDefinition = DependencyDefinition.fromParameter(constructorParameter);
            constructorArgs[i] = DependencyUtils.resolveDependency(dependencyDefinition, DEPENDENCY_DECLARED_AS, dependencyResolvers);
        }
        return constructorArgs;
    }

    @Builder
    static ClassElementAssembler from(
            String elementName,
            Class<?> elementType,
            ClassElementSource classElementSource,
            List<DependencyResolver> dependencyResolvers
    ) {
        return new ClassElementAssembler(elementName, elementType, classElementSource, dependencyResolvers);
    }

}
