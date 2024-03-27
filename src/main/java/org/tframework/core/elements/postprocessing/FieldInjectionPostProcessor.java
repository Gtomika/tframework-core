/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.postprocessing;

import java.lang.reflect.Field;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.InjectAnnotationScanner;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolverAggregator;
import org.tframework.core.reflection.field.FieldFilter;
import org.tframework.core.reflection.field.FieldScanner;
import org.tframework.core.reflection.field.FieldSetter;

/**
 * This {@link ElementInstancePostProcessor} is responsible for injecting the dependencies
 * into the appropriately annotated fields of the element instance.
 */
@Slf4j
@Builder
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FieldInjectionPostProcessor implements ElementInstancePostProcessor {

    static final String DEPENDENCY_DECLARED_AS_FIELD = "Field";

    private final InjectAnnotationScanner injectAnnotationScanner;
    private final FieldScanner fieldScanner;
    private final FieldFilter fieldFilter;
    private final FieldSetter fieldSetter;
    private final DependencyResolverAggregator dependencyResolver;

    @Override
    public void postProcessInstance(ElementContext elementContext, Object instance) {
        var dependencyFields = fieldScanner.getAllFields(elementContext.getType())
                .stream()
                .filter(this::isCandidateForDependencyInjection)
                .peek(field -> log.debug("Element context '{}' has field '{}' which is a candidate for dependency injection",
                        elementContext.getName(), field.getName()))
                .toList();

        dependencyFields.forEach(field -> setDependencyField(elementContext, instance, field));
    }

    private boolean isCandidateForDependencyInjection(Field field) {
        return !fieldFilter.isStatic(field) && injectAnnotationScanner.hasAnyInjectAnnotations(field);
    }

    private void setDependencyField(ElementContext elementContext, Object instance, Field field) {
        var dependencyDefinition = DependencyDefinition.fromField(field);
        Object resolvedDependency = dependencyResolver.resolveDependency(
                dependencyDefinition,
                elementContext,
                ElementDependencyGraph.empty(),
                DEPENDENCY_DECLARED_AS_FIELD
        );
        fieldSetter.setFieldValue(instance, field, resolvedDependency);
    }

}
