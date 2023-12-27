/* Licensed under Apache-2.0 2023. */
package org.tframework.core.annotations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utilities to create {@link AnnotationScanner}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AnnotationScannersFactory {

    /**
     * Creates a {@link ComposedAnnotationScanner} that uses {@link ExtendedAnnotationMatcher}
     * to find annotations on classes.
     */
    public static ComposedAnnotationScanner createComposedAnnotationScanner() {
        var matcher = AnnotationMatchersFactory.createExtendedAnnotationMatcher();
        return new ComposedAnnotationScanner(matcher);
    }

}
