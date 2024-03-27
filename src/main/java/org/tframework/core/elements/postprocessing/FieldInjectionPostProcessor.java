/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.postprocessing;

import java.lang.reflect.Field;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.InjectElement;
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
 * into the appropriately annotated fields of the element instance. The fields
 * are candidates for injection, if all conditions are met:
 * <ul>
 *     <li>The field is not static.</li>
 *     <li>The field is not final.</li>
 *     <li>The field is annotated with some {@code InjectX} annotation, such as {@link InjectElement}.</li>
 * </ul>
 * visibility is not relevant, fields can be private as well.
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
                .filter(field -> isCandidateForFieldInjection(elementContext, field))
                .peek(field -> log.debug("Element context '{}', field '{}': candidate for field injection",
                        elementContext.getName(), field.getName()))
                .toList();

        dependencyFields.forEach(field -> setDependencyField(elementContext, instance, field));
    }

    private boolean isCandidateForFieldInjection(ElementContext elementContext, Field field) {
        if(!injectAnnotationScanner.hasAnyInjectAnnotations(field)) {
            log.debug("Element context '{}', field '{}': not a candidate for field injection, because it is not '@InjectX' annotated",
                    elementContext.getName(), field.getName());
            return false;
        }
        //field is @InjectX annotated, but that is not enough to be a candidate

        if(fieldFilter.isStatic(field)) {
            log.warn("Element context '{}', field '{}' is STATIC, so it will be ignored by field injection",
                    elementContext.getName(), field.getName());
            return false;
        }
        if(fieldFilter.isFinal(field)) {
            log.warn("Element context '{}', field '{}' is FINAL, so it will be ignored by field injection",
                    elementContext.getName(), field.getName());
            return false;
        }
        return true;
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
        log.debug("Element context '{}', field '{}': injected a dependency: {}",
                elementContext.getName(), field.getName(), resolvedDependency);
    }

}
