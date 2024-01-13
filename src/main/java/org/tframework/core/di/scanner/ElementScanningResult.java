/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import org.tframework.core.di.annotations.Element;

/**
 * The result of various element scanners operations.
 * @param elementAnnotation The {@link Element} annotation found on the element.
 * @param annotationSource The component that the {@link Element} annotation was found on.
 * @param <T> Type of the annotation source component.
 * @see ElementScanner
 */
public record ElementScanningResult<T>(
        Element elementAnnotation,
        T annotationSource
) {
}
