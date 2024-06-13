/* Licensed under Apache-2.0 2024. */
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
        Integer e1Priority = annotationScanner.scanOneStrict(e1.getType(), Priority.class)
                .map(Priority::value)
                .orElse(Priority.DEFAULT);
        Integer e2Priority = annotationScanner.scanOneStrict(e2.getType(), Priority.class)
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
