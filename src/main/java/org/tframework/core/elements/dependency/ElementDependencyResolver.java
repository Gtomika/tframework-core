/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency;

import lombok.extern.slf4j.Slf4j;
import org.tframework.core.annotations.AnnotationMatchingResult;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Optional;

/**
 * This {@link DependencyResolver} is responsible for resolving dependencies that are annotated with
 * {@link InjectElement}. If the dependency is not annotated with {@link InjectElement}, this
 * resolver will ignore it.
 */
@Slf4j
public class ElementDependencyResolver extends DependencyResolver {

    private final InjectAnnotationScanner injectAnnotationScanner;

    ElementDependencyResolver(ElementsContainer dependencySource, InjectAnnotationScanner injectAnnotationScanner) {
        super(dependencySource);
        this.injectAnnotationScanner = injectAnnotationScanner;
    }

    @Override
    public Optional<Object> resolveDependency(DependencyDefinition dependencyDefinition) {
        var matchingResult = matchInjectAnnotation(dependencyDefinition.annotationSource());
        if(matchingResult.matches()) {
            InjectElement injectAnnotation = matchingResult.matchedAnnotations().getFirst();
            String dependencyName = getElementDependencyName(injectAnnotation, dependencyDefinition.dependencyType());
            log.debug("Attempting to resolve dependency with name '{}' from the elements...", dependencyName);
            try {
                Object resolvedDependency = dependencySource.requestDependency(dependencyName);
                log.debug("Resolved dependency with name '{}' from the elements: {}", dependencyName, resolvedDependency);
                return Optional.of(resolvedDependency);
            } catch (Exception e) {
                log.debug("Failed to resolve dependency with name '{}' from the elements", dependencyName, e);
                return Optional.empty();
            }
        } else {
            log.debug("Dependency definition '{}' is not annotated with '@InjectElement', cannot resolve it", dependencyDefinition);
            return Optional.empty();
        }
    }

    private AnnotationMatchingResult<InjectElement> matchInjectAnnotation(AnnotatedElement dependencyDefinition) {
        return injectAnnotationScanner.findInjectAnnotation(dependencyDefinition, InjectElement.class)
                .stream()
                .findAny()
                .map(injectElement -> new AnnotationMatchingResult<>(true, List.of(injectElement)))
                .orElseGet(() -> new AnnotationMatchingResult<>(false, List.of()));
    }

    private String getElementDependencyName(InjectElement injectAnnotation, Class<?> dependencyType) {
        if(Element.NAME_NOT_SPECIFIED.equals(injectAnnotation.value())) {
            return ElementUtils.getElementNameByType(dependencyType);
        } else {
            return injectAnnotation.value();
        }
    }

}
