/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.tframework.core.annotations.AnnotationScanner;
import org.tframework.core.classes.ClassFilter;
import org.tframework.core.di.annotations.Element;
import org.tframework.core.properties.PropertiesContainer;

/**
 * Abstract base class for all implementations that search for the {@link Element} annotation on classes.
 * @see ElementClassScannersFactory
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ElementClassScanner {

    private final ClassFilter classFilter;
    private final AnnotationScanner annotationScanner;
    protected final PropertiesContainer propertiesContainer;

    /**
     * Scans for classes that could be elements. Does not actually filter them by the
     * {@link Element} annotation. Subclasses should implement their own logic for finding potential elements,
     * For example: scanning a package, or scanning the classpath.
     */
    protected abstract Set<Class<?>> scanPotentialElements();

    /**
     * Scans for classes that are elements, using the underlying implementation
     * to find potential elements, and then filtering them.
     * @return A {@link Set} of classes that are elements (the internal {@link ClassFilter} determined that they
     * are annotated with {@link Element}).
     */
    public Set<Class<?>> scanElements() {
        return filterElements(scanPotentialElements());
    }

    protected Set<Class<?>> filterElements(Set<Class<?>> classes) {
        return new HashSet<>(classFilter.filterByAnnotation(classes, Element.class, annotationScanner));
    }
}
