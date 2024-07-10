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
package org.tframework.core.elements.scanner;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;
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
                .map(result -> new ElementScanningResult<Class<?>>(result.annotation(), result.annotationSource()))
                .collect(Collectors.toSet());
    }

    //performs strict scanning for the Element annotation on the given classes
    //at most one Element annotation is allowed per class, to avoid ambiguity
    protected Stream<AnnotationFilteringResult<Element, Class<?>>> filterElements(Set<Class<?>> classes) {
        return classFilter.filterByAnnotation(classes, Element.class, annotationScanner, true)
                .stream()
                // don't want to find annotations as possible elements
                // if an annotation is annotated with @Element, that is a composed annotation, not an element
                .filter(result -> !result.annotationSource().isAnnotation());
    }
}
