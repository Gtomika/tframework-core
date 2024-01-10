/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.classes;

import java.lang.annotation.Annotation;
import java.util.Collection;
import org.tframework.core.annotations.AnnotationScanner;

/**
 * The class filter is responsible for selecting classes that match certain criteria. The class
 * filter does not search for classes, only filters, usually the classes are provided by a {@link ClassScanner}.
 * @see ClassFiltersFactory
 */
public interface ClassFilter {

    /**
     * Filters the classes so that only those with an annotation on them will be returned.
     * @param classes The classes to filter.
     * @param annotationClass The annotation to filter on.
     * @param annotationScanner An {@link AnnotationScanner} that is going to check if {@code annotationClass}
     *                          is present on the classes.
     * @return Filtered collection of classes.
     */
    Collection<Class<?>> filterByAnnotation(
            Collection<Class<?>> classes,
            Class<? extends Annotation> annotationClass,
            AnnotationScanner annotationScanner
    );

    /**
     * Filters the classes so that only those which can be assigned to {@code superClass} will be returned.
     * @param classes The classes to filter.
     * @param superClass The super class to filter for. This can also be an interface.
     */
    Collection<Class<?>> filterBySuperClass(Collection<Class<?>> classes, Class<?> superClass);

}
