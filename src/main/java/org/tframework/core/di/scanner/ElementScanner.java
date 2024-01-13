/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import java.lang.reflect.AnnotatedElement;
import java.util.Set;

/**
 * Top level interface for all element scanners. These are responsible for finding
 * elements in the application.
 * @param <T> The type of the component that is annotated as an element.
 */
public interface ElementScanner<T extends AnnotatedElement> {

    /**
     * @return A set of {@link ElementScanningResult}s, each
     * containing the element annotation and the element that was annotated with it.
     */
    Set<ElementScanningResult<T>> scanElements();

}
