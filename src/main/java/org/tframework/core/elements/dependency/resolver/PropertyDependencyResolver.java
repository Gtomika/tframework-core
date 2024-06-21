/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.InjectAnnotationScanner;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.reflection.annotations.AnnotationMatchingResult;

/**
 * This {@link BasicDependencyResolver} is responsible for resolving dependencies that are annotated with
 * {@link InjectProperty}. If the dependency is not annotated with {@link InjectProperty}, this
 * resolver will ignore it.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PropertyDependencyResolver implements BasicDependencyResolver {

    private final PropertiesContainer propertiesContainer;
    private final InjectAnnotationScanner injectAnnotationScanner;

    @Override
    public Optional<Object> resolveDependency(DependencyDefinition dependencyDefinition) {
        var matchingResult = matchPropertiesInjectAnnotation(dependencyDefinition.annotationSource());
        if(matchingResult.matches()) {
            InjectProperty injectAnnotation = matchingResult.matchedAnnotations().getFirst();
            String dependencyName = injectAnnotation.value();
            log.debug("Attempting to resolve dependency with name '{}' from the properties...", dependencyName);
            try {
                Object resolvedDependency;
                if(injectAnnotation.defaultValue().equals(InjectProperty.DEFAULT_VALUE_NOT_PROVIDED)) {
                    resolvedDependency = propertiesContainer.getPropertyValueNonGeneric(
                            dependencyName,
                            dependencyDefinition.dependencyType()
                    );
                } else {
                    resolvedDependency = propertiesContainer.getPropertyValueNonGeneric(
                            dependencyName,
                            dependencyDefinition.dependencyType(),
                            injectAnnotation.defaultValue()
                    );
                }
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
