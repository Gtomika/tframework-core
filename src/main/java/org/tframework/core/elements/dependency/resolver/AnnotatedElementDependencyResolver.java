/*
Copyright 2024 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core.elements.dependency.resolver;

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
import org.tframework.core.elements.dependency.InjectAnnotationHelper;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.handler.SpecialDependencyHandlerAggregator;
import org.tframework.core.elements.dependency.resolver.helper.ElementDependencyResolverHelper;
import org.tframework.core.reflection.annotations.AnnotationMatchingResult;
import org.tframework.core.reflection.annotations.PreScannedAnnotations;

/**
 * This {@link ElementDependencyResolver} is responsible for resolving dependencies that are annotated with
 * {@link InjectElement}. If the dependency is not annotated with {@link InjectElement}, this
 * resolver will ignore it.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AnnotatedElementDependencyResolver implements ElementDependencyResolver {

    private final ElementsContainer elementsContainer;
    private final InjectAnnotationHelper injectAnnotationHelper;
    private final ElementDependencyResolverHelper byNameResolverHelper;
    private final ElementDependencyResolverHelper byTypeResolverHelper;
    private final SpecialDependencyHandlerAggregator specialDependencyHandlerAggregator;

    @Override
    public Optional<Object> resolveDependency(
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph,
            PreScannedAnnotations preScannedAnnotations
    ) {
        var matchingResult = matchInjectAnnotation(preScannedAnnotations);
        if(matchingResult.matches()) {
            InjectElement injectAnnotation = matchingResult.matchedAnnotations().getFirst();
            try {
                if(ElementUtils.isNamedElementInjection(injectAnnotation)) {
                    String dependencyName = injectAnnotation.value();
                    var resolvedDependency = byNameResolverHelper.resolveElementDependency(
                            elementsContainer, originalElementContext, dependencyDefinition, dependencyName, dependencyGraph
                    );
                    return Optional.of(resolvedDependency);
                } else {
                    var handledSpecialResult = specialDependencyHandlerAggregator.handleDependency(
                            elementsContainer, dependencyDefinition, originalElementContext, dependencyGraph
                    );
                    if(handledSpecialResult.isPresent()) {
                        return handledSpecialResult;
                    } else {
                        var resolvedDependency = byTypeResolverHelper.resolveElementDependency(
                                elementsContainer, originalElementContext, dependencyDefinition, null, dependencyGraph
                        );
                        return Optional.of(resolvedDependency);
                    }
                }
            } catch (Exception e) {
                log.debug("Failed to resolve dependency from the elements", e);
                return Optional.empty();
            }
        } else {
            log.debug("Dependency definition '{}' is not annotated with '@InjectElement', cannot resolve it", dependencyDefinition);
            return Optional.empty();
        }
    }

    private AnnotationMatchingResult<InjectElement> matchInjectAnnotation(PreScannedAnnotations preScannedAnnotations) {
        return injectAnnotationHelper.findInjectAnnotation(preScannedAnnotations, InjectElement.class)
                .stream()
                .findAny()
                .map(injectElement -> new AnnotationMatchingResult<>(true, List.of(injectElement)))
                .orElseGet(() -> new AnnotationMatchingResult<>(false, List.of()));
    }

}
