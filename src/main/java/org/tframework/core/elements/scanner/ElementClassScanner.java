/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.scanner;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.classes.ClassFilter;

/**
 * Abstract base class for all implementations that search for the {@link Element} annotation on classes.
 * @see ElementScannersFactory
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ElementClassScanner implements ElementScanner<Class<?>> {

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
     * @return A set of {@link ElementScanningResult}s, each containing the {@link Element} annotation
     * and the class that was annotated with it.
     */
    public Set<ElementScanningResult<Class<?>>> scanElements() {
        return filterElements(scanPotentialElements())
                .stream()
                .map(result -> new ElementScanningResult<Class<?>>(result.annotation(), result.annotationSource()))
                .collect(Collectors.toSet());
    }

    //performs strict scanning for the Element annotation on the given classes
    //at most one Element annotation is allowed per class, to avoid ambiguity
    protected Collection<AnnotationFilteringResult<Element, Class<?>>> filterElements(Set<Class<?>> classes) {
        return classFilter.filterByAnnotation(classes, Element.class, annotationScanner, true);
    }
}
