/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import java.lang.annotation.Annotation;
import java.util.List;
import org.tframework.core.annotations.AnnotationScanner;

/**
 * The class filter is responsible for selecting classes that match certain criteria. The class
 * filter does not search for classes, only filters, usually the classes are provided by a {@link ClassScanner}.
 * @see ClassScanner
 */
public interface ClassFilter {

    /**
     * Filters the classes so that only those with an annotation on them will be returned.
     * @param classes The classes to filter.
     * @param annotationClass The annotation to filter on.
     * @param annotationScanner An {@link AnnotationScanner} that is going to check if {@code annotationClass}
     *                          is present on the classes.
     * @return Filtered list of classes.
     */
    List<Class<?>> filterByAnnotation(
            List<Class<?>> classes,
            Class<? extends Annotation> annotationClass,
            AnnotationScanner annotationScanner
    );

    /**
     * Filters the classes so that only those which implement an interface are returned.
     * @param classes The classes to filter.
     * @param interfaceClass The interface to filter for, this {@link Class} should actually be an interface.
     * @return Filtered list of classes
     * @throws NotAnInterfaceException If {@code interfaceClass} is not an interface.
     */
    List<Class<?>> filterByInterface(List<Class<?>> classes, Class<?> interfaceClass);

    /**
     * Can be used by implementations to perform an interface check and throw
     * an {@link NotAnInterfaceException} if the check is failed.
     */
    default void checkIfInterface(Class<?> clazz) {
        if(!clazz.isInterface()) {
            throw new NotAnInterfaceException(clazz);
        }
    }
}
