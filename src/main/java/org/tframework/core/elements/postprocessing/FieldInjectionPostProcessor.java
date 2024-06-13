/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.postprocessing;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.annotations.Priority;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.InjectAnnotationScanner;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolverAggregator;
import org.tframework.core.elements.dependency.resolver.DependencyResolverConfig;
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
@Element
@Priority(Priority.HIGHEST)
public class FieldInjectionPostProcessor implements ElementInstancePostProcessor {

    static final String DEPENDENCY_DECLARED_AS_FIELD = "Field";

    private final InjectAnnotationScanner injectAnnotationScanner;
    private final FieldScanner fieldScanner;
    private final FieldFilter fieldFilter;
    private final FieldSetter fieldSetter;
    private final DependencyResolverAggregator dependencyResolver;

    public FieldInjectionPostProcessor(
            InjectAnnotationScanner injectAnnotationScanner,
            FieldScanner fieldScanner,
            FieldFilter fieldFilter,
            FieldSetter fieldSetter,
            @InjectElement(DependencyResolverConfig.FIELD_DEPENDENCY_RESOLVER_ELEMENT_NAME) DependencyResolverAggregator dependencyResolver
    ) {
        this.injectAnnotationScanner = injectAnnotationScanner;
        this.fieldScanner = fieldScanner;
        this.fieldFilter = fieldFilter;
        this.fieldSetter = fieldSetter;
        this.dependencyResolver = dependencyResolver;
    }

    @Override
    public void postProcessInstance(ElementContext elementContext, Object instance) {
        fieldScanner.getAllFields(elementContext.getType())
                .stream()
                .filter(injectAnnotationScanner::hasAnyInjectAnnotations)
                .peek(field -> {
                    isValidFieldForInjection(elementContext, field);
                    log.debug("Element context '{}', field '{}': candidate for field injection",
                            elementContext.getName(), field.getName());
                })
                .forEach(field ->  setDependencyField(elementContext, instance, field));
    }

    private void isValidFieldForInjection(ElementContext elementContext, Field field) {
        List<String> problems = new LinkedList<>();

        if(fieldFilter.isStatic(field)) {
            problems.add("Field cannot be static");
        }
        if(fieldFilter.isFinal(field)) {
            problems.add("Field cannot be final");
        }

        if(!problems.isEmpty()) {
            throw new FieldInjectionException(field, elementContext, problems);
        }
    }

    private void setDependencyField(ElementContext elementContext, Object instance, Field field) {
        try {
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
        } catch (Exception e) {
            throw new FieldInjectionException(field, elementContext, e);
        }
    }

}
