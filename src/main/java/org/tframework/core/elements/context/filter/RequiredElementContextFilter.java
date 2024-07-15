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
package org.tframework.core.elements.context.filter;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.Application;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.filter.annotation.RequiredElement;
import org.tframework.core.elements.context.filter.exception.ElementFilterException;
import org.tframework.core.reflection.annotations.AnnotationScanner;

/**
 * This {@link ElementContextFilter} implementation filters based on the rules
 * described on {@link RequiredElement}
 */
@Slf4j
@Element
@RequiredArgsConstructor
public class RequiredElementContextFilter implements ElementContextFilter {

    private final AnnotationScanner annotationScanner;

    @Override
    public boolean discardElementContext(ElementContext elementContext, Application application) {
        return annotationScanner.scan(elementContext.getSource().annotatedSource(), RequiredElement.class)
                .stream()
                .peek(annotation -> validateAnnotation(elementContext, annotation))
                .anyMatch(annotation -> !requiredElementFulfilled(elementContext, annotation, application.getElementsContainer()));
    }

    @Override
    public Set<FilteringRound> applyInRound() {
        /*
        - We must apply this in the second round, because if another filter removes the required element,
        (in round 1) then this filter should not see it (in round 2).
        - We must also apply it in the third round, because if THIS filter removes the required element
        (in round 2), then the next filter should not see it (in round 3).
         */
        return Set.of(FilteringRound.SECOND_ROUND, FilteringRound.THIRD_ROUND);
    }

    private void validateAnnotation(ElementContext elementContext, RequiredElement annotation) {
        if(annotation.name().equals(RequiredElement.NAME_NOT_PROVIDED) && annotation.type().equals(RequiredElement.TYPE_NOT_PROVIDED)) {
            throw new ElementFilterException("@RequiredElement on " + elementContext.getName() +
                    " must have either name or type defined");
        }
        if(!annotation.name().equals(RequiredElement.NAME_NOT_PROVIDED) && !annotation.type().equals(RequiredElement.TYPE_NOT_PROVIDED)) {
            throw new ElementFilterException("@RequiredElement on " + elementContext.getName() +
                    " must have either name or type defined, but not both");
        }
    }

    private boolean requiredElementFulfilled(
            ElementContext elementContext,
            RequiredElement annotation,
            ElementsContainer elementsContainer
    ) {
        if(!annotation.name().equals(RequiredElement.NAME_NOT_PROVIDED)) {
            boolean hasContextWithName = elementsContainer.hasElementContext(annotation.name());
            if(!hasContextWithName) {
                log.debug("Element '{}' required another one with name '{}', which was not found. Will be filtered out.",
                        elementContext.getName(), annotation.name());
            }
            return hasContextWithName;
        } else {
            boolean hasContextWithType = elementsContainer.hasElementContext(annotation.type());
            if(!hasContextWithType) {
                log.debug("Element '{}' required another one with type assignable to '{}', which was not found. Will be filtered out.",
                        elementContext.getName(), annotation.type().getName());
            }
            return hasContextWithType;
        }
    }
}
