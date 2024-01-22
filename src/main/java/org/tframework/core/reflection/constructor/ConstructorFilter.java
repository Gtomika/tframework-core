/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.constructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Set;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.MultipleAnnotationsScannedException;

/**
 * The constructor filter is responsible for selecting constructors that match certain criteria. These constructors
 * are usually provided by a {@link ConstructorScanner}.
 */
public interface ConstructorFilter {

    /**
     * Filters the constructors so that only those with an annotation on them will be returned. If {@code strict} is true,
     * then only at most one annotation of the given type is allowed on a constructor.
     * @param constructors The constructors to filter.
     * @param annotationClass The annotation to filter on.
     * @param annotationScanner An {@link AnnotationScanner} that is going to check if {@code annotationClass} is present on the constructors.
     * @param strict Enables string filtering. If true, then only at most one annotation of the given type is allowed on a constructor.
     * @param <A> Type of the annotation to find.
     * @return {@link AnnotationFilteringResult}s, each containing the annotation and the constructor that was annotated with it.
     * @throws MultipleAnnotationsScannedException If {@code strict} is
     * true and more than one annotation of the given type is found on a constructor.
     */
    <A extends Annotation> Set<AnnotationFilteringResult<A, Constructor<?>>> filterByAnnotation(
            Set<Constructor<?>> constructors,
            Class<A> annotationClass,
            AnnotationScanner annotationScanner,
            boolean strict
    );

    /**
     * Filters the constructors so that only those which are public will be returned.
     * @param constructors The constructors to filter.
     */
    Set<Constructor<?>> filterPublicConstructors(Set<Constructor<?>> constructors);

}
