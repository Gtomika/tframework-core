/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import org.tframework.core.annotations.AnnotationScanner;

/**
 * A method filter selects methods based on some criteria. The methods are
 * usually found by a {@link MethodScanner} and then processed by a filter.
 */
public interface MethodFilter {

    /**
     * Filters methods by a given annotation.
     * @param methods {@link Set} of methods to filter.
     * @param annotationClass Annotation to filter by.
     * @param annotationScanner An {@link AnnotationScanner} to use to determine if the annotation is present.
     * @return A {@link Set} of methods that have the given annotation, according to {@code annotationScanner}.
     */
    Set<Method> filterByAnnotation(
            Set<Method> methods,
            Class<? extends Annotation> annotationClass,
            AnnotationScanner annotationScanner
    );

}
