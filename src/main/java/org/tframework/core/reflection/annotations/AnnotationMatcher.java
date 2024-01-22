/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.annotations;

import java.lang.annotation.Annotation;

/**
 * Performs matching of different annotations. Annotation matching can be considered as an extension
 * over simple annotation type equality.
 * @see AnnotationMatchingResult
 * @see AnnotationMatchersFactory
 */
public interface AnnotationMatcher {

    /**
     * Performs the annotation matching: determines if the {@code annotationToMatch} can be matched
     * to the type {@code expectedAnnotationClass}.
     * @param annotationToMatch The annotation to match against the {@code expectedAnnotationClass}.
     * @return An {@link AnnotationMatchingResult} with the match status.
     */
    <A extends Annotation> AnnotationMatchingResult<A> matches(Class<A> expectedAnnotationClass, Annotation annotationToMatch);

}
