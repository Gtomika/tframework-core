/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.tframework.core.annotations.AnnotationScanner;
import org.tframework.core.classes.ClassFilter;
import org.tframework.core.di.annotations.Element;

/**
 * Abstract base class for all implementations that search for the {@link Element} annotation on classes.
 * @see ElementClassScannersFactory
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ElementClassScanner {

    private final ClassFilter classFilter;
    private final AnnotationScanner annotationScanner;

    /**
     * Scans for classes that could be elements. Does not actually filter them by the
     * {@link Element} annotation. Subclasses should implement their own logic for finding potential elements,
     * For example: scanning a package, or scanning the classpath.
     */
    protected abstract List<Class<?>> scanPotentialElements();

    /**
     * Scans for classes that are elements, using the underlying implementation
     * to find potential elements, and then filtering them.
     * @return A list of classes that are elements (the internal {@link ClassFilter} determined that they
     * are annotated with {@link Element}).
     */
    public List<Class<?>> scanElements() {
        return filterElements(scanPotentialElements());
    }

    protected List<Class<?>> filterElements(List<Class<?>> classes) {
        return classFilter.filterByAnnotation(classes, Element.class, annotationScanner);
    }
}
