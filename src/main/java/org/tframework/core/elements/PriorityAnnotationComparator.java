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
package org.tframework.core.elements;

import java.util.Comparator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.tframework.core.elements.annotations.Priority;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;

/**
 * Responsible for ordering any objects based on their {@link Priority} annotation, while
 * also handling the case when the annotation is not present.
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PriorityAnnotationComparator implements Comparator<ElementContext> {

    private final AnnotationScanner annotationScanner;

    @Override
    public int compare(ElementContext e1, ElementContext e2) {
        //TODO #110: replace annotation scanning
        Integer e1Priority = annotationScanner.scanOneStrict(e1.getSource().annotatedSource(), Priority.class)
                .map(Priority::value)
                .orElse(Priority.DEFAULT);
        Integer e2Priority = annotationScanner.scanOneStrict(e2.getSource().annotatedSource(), Priority.class)
                .map(Priority::value)
                .orElse(Priority.DEFAULT);
        //reversed because higher priority means it should come first
        return e2Priority.compareTo(e1Priority);
    }

    /**
     * Creates a default comparator which supports composed {@link Priority} annotations.
     */
    public static PriorityAnnotationComparator create() {
        return new PriorityAnnotationComparator(AnnotationScannersFactory.createComposedAnnotationScanner());
    }
}
