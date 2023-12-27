/* Licensed under Apache-2.0 2023. */
package org.tframework.core.annotations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utilities to create {@link AnnotationMatcher}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AnnotationMatchersFactory {

    /**
     * Creates an {@link ExtendedAnnotationMatcher}.
     */
    public static ExtendedAnnotationMatcher createExtendedAnnotationMatcher() {
        return new ExtendedAnnotationMatcher();
    }

}
