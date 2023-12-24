/* Licensed under Apache-2.0 2023. */
package org.tframework.core.annotations;

import java.lang.annotation.Annotation;

/**
 * Performs matching of different annotations. All implementations should be stateless.
 * @see AnnotationMatchingResult
 */
public interface AnnotationMatcher {

    /**
     * Performs the annotation matching: determines of the {@code annotationToMatch} matches the annotation class
     * {@code expectedAnnotationClass}.
     * @param annotationToMatch The annotation to match against the {@code expectedAnnotationClass}.
     * @return An {@link AnnotationMatchingResult} with the match status.
     */
    <A extends Annotation> AnnotationMatchingResult<A> matches(Class<A> expectedAnnotationClass, Annotation annotationToMatch);

}
