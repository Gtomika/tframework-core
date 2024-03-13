/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.InjectAnnotationScanner;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.reflection.annotations.AnnotationMatchingResult;

/**
 * This {@link ElementDependencyResolver} is responsible for resolving dependencies that are annotated with
 * {@link InjectElement}. If the dependency is not annotated with {@link InjectElement}, this
 * resolver will ignore it.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AnnotatedElementDependencyResolver implements ElementDependencyResolver {

    private final ElementsContainer elementsContainer;
    private final InjectAnnotationScanner injectAnnotationScanner;

    @Override
    public Optional<Object> resolveDependency(
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph
    ) {
        var matchingResult = matchInjectAnnotation(dependencyDefinition.annotationSource());
        if(matchingResult.matches()) {
            InjectElement injectAnnotation = matchingResult.matchedAnnotations().getFirst();
            try {
                ElementContext dependencyElementContext;
                if(ElementUtils.isNamedElementInjection(injectAnnotation)) {
                    String dependencyName = injectAnnotation.value();
                    log.debug("Attempting to resolve dependency with name '{}' from the elements", dependencyName);
                    dependencyElementContext = elementsContainer.getElementContext(dependencyName);
                } else {
                    log.debug("Attempting to resolve dependency with type '{}' from the elements", dependencyDefinition.dependencyType());
                    dependencyElementContext = elementsContainer.getElementContext(dependencyDefinition.dependencyType());
                }

                dependencyGraph.addDependency(originalElementContext, dependencyElementContext);
                Object resolvedDependency = dependencyElementContext.requestInstance(dependencyGraph);
                log.debug("Resolved dependency from the elements: {}", resolvedDependency);
                return Optional.of(resolvedDependency);
            } catch (Exception e) {
                log.debug("Failed to resolve dependency from the elements", e);
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

}
