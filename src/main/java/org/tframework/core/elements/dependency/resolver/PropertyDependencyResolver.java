/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.InjectAnnotationScanner;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.reflection.annotations.AnnotationMatchingResult;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Optional;

/**
 * This {@link BasicDependencyResolver} is responsible for resolving dependencies that are annotated with
 * {@link InjectProperty}. If the dependency is not annotated with {@link InjectProperty}, this
 * resolver will ignore it.
 */
@Slf4j
public class PropertyDependencyResolver extends BasicDependencyResolver {

    private final InjectAnnotationScanner injectAnnotationScanner;

    PropertyDependencyResolver(PropertiesContainer dependencySource, InjectAnnotationScanner injectAnnotationScanner) {
        super(dependencySource);
        this.injectAnnotationScanner = injectAnnotationScanner;
    }

    @Override
    public Optional<Object> resolveDependency(DependencyDefinition dependencyDefinition) {
        var matchingResult = matchPropertiesInjectAnnotation(dependencyDefinition.annotationSource());
        if(matchingResult.matches()) {
            InjectProperty injectAnnotation = matchingResult.matchedAnnotations().getFirst();
            String dependencyName = injectAnnotation.value();
            log.debug("Attempting to resolve dependency with name '{}' from the properties...", dependencyName);
            try {
                Object resolvedDependency = dependencySource.requestDependency(dependencyName);
                log.debug("Resolved dependency with name '{}' from the properties: {}", dependencyName, resolvedDependency);
                return Optional.of(resolvedDependency);
            } catch (Exception e) {
                log.debug("Failed to resolve dependency with name '{}' from the properties", dependencyName, e);
                return Optional.empty();
            }
        } else {
            log.debug("Dependency definition '{}' is not annotated with '@InjectProperty', cannot resolve it", dependencyDefinition);
            return Optional.empty();
        }
    }

    private AnnotationMatchingResult<InjectProperty> matchPropertiesInjectAnnotation(AnnotatedElement dependencyDefinition) {
        return injectAnnotationScanner.findInjectAnnotation(dependencyDefinition, InjectProperty.class)
                .stream()
                .findAny()
                .map(injectProperty -> new AnnotationMatchingResult<>(true, List.of(injectProperty)))
                .orElseGet(() -> new AnnotationMatchingResult<>(false, List.of()));
    }
}
