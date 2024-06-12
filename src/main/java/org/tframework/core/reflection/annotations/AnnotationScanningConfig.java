/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.annotations;

import org.tframework.core.elements.annotations.Element;

/**
 * A global configuration for all things related to annotation scanning.
 */
@Element
public class AnnotationScanningConfig {

    /**
     * Provides the default annotation scanner for the framework as an element, so it can be injected.
     */
    @Element
    public AnnotationScanner provideDefaultAnnotationScanner() {
        return AnnotationScannersFactory.createComposedAnnotationScanner();
    }

}
