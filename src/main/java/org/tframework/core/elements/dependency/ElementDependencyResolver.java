/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.annotations.AnnotationMatchingResult;
import org.tframework.core.elements.annotations.InjectElement;

/**
 * This {@link DependencyResolver} is responsible for resolving dependencies that are annotated with
 * {@link InjectElement}.
 */
@Slf4j //TODO unit test
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ElementDependencyResolver implements DependencyResolver {

    private final InjectAnnotationScanner injectAnnotationScanner;

    @Override
    public Optional<Object> resolveDependency(DependencySource dependencySource, AnnotatedElement dependencyDefinition) {
        var matchingResult = matchInjectAnnotation(dependencyDefinition);
        if(matchingResult.matches()) {
            InjectElement injectAnnotation = matchingResult.matchedAnnotations().get(0);
            String dependencyName = injectAnnotation.value();
            log.debug("Attempting to resolve dependency with name '{}' from the elements...", dependencyName);
            Object resolvedDependency = dependencySource.requestDependency(dependencyName);
            log.debug("Resolved dependency with name '{}' from the elements: {}", dependencyName, resolvedDependency);
            return Optional.of(resolvedDependency);
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
}
