/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Contains the result of filtering by annotation.
 * @param annotation The annotation that was found on the class.
 * @param annotationSource The {@link AnnotatedElement} that was annotated.
 * @param <A> Type of {@code annotationSource}.
 */
public record AnnotationFilteringResult<A extends Annotation, S extends AnnotatedElement>(
        A annotation,
        S annotationSource
) {
}
