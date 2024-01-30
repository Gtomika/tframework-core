/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.classes;

import java.lang.annotation.Annotation;
import java.util.Collection;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.annotations.MultipleAnnotationsScannedException;

/**
 * The class filter is responsible for selecting classes that match certain criteria. The class
 * filter does not search for classes, only filters, usually the classes are provided by a {@link ClassScanner}.
 * @see ClassFiltersFactory
 */
public interface ClassFilter {

    /**
     * Filters the classes so that only those with an annotation on them will be returned. If {@code strict} is true,
     * then only at most one annotation of the given type is allowed on a class.
     * @param classes The classes to filter.
     * @param annotationClass The annotation to filter on.
     * @param annotationScanner An {@link AnnotationScanner} that is going to check if {@code annotationClass} is present on the classes.
     * @param strict Enables string filtering. If true, then only at most one annotation of the given type is allowed on a class.
     * @return {@link AnnotationFilteringResult}s, each containing the annotation and the class that was annotated with it.
     * @param <A> Type of the annotation to find.
     * @throws MultipleAnnotationsScannedException If {@code strict} is
     * true and more than one annotation of the given type is found on a class.
     */
    <A extends Annotation> Collection<AnnotationFilteringResult<A, Class<?>>> filterByAnnotation(
            Collection<Class<?>> classes,
            Class<A> annotationClass,
            AnnotationScanner annotationScanner,
            boolean strict
    );

    /**
     * Filters the classes so that only those which can be assigned to {@code superClass} will be returned.
     * @param classes The classes to filter.
     * @param superClass The super class to filter for. This can also be an interface.
     */
    Collection<Class<?>> filterBySuperClass(Collection<Class<?>> classes, Class<?> superClass);

}
