/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.annotations;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Contains the result of an annotation matching: {@link AnnotationMatcher#matches(Class, Annotation)}.
 * @param matches If there was a match.
 * @param matchedAnnotations All matched annotations. If {@code matches} is false, this list will be always empty.
 * @param <A> Type of the matched annotation.
 */
public record AnnotationMatchingResult<A extends Annotation>(
    boolean matches,
    List<A> matchedAnnotations
) {
}
