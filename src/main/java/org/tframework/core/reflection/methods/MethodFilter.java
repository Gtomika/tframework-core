/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import org.tframework.core.annotations.AnnotationScanner;
import org.tframework.core.reflection.AnnotationFilteringResult;

/**
 * A method filter selects methods based on some criteria. The methods are
 * usually found by a {@link MethodScanner} and then processed by a filter.
 */
public interface MethodFilter {

    /**
     * Filters methods by a given annotation.
     * @param methods Methods to filter.
     * @param annotationClass Annotation to filter by.
     * @param annotationScanner An {@link AnnotationScanner} to use to determine if the annotation is present.
     * @return A collection of {@link AnnotationFilteringResult}s, one for each method that was annotated.
     * @param <A> Type of annotation.
     */
    <A extends Annotation> Collection<AnnotationFilteringResult<A, Method>> filterByAnnotation(
            Collection<Method> methods,
            Class<A> annotationClass,
            AnnotationScanner annotationScanner,
            boolean strict
    );

}
