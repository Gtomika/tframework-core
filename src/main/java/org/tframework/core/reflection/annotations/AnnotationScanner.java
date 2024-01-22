/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Optional;

/**
 * Interface for classes that can detect ("scan") annotations on a provided components, such
 * as classes or methods.
 * @see AnnotationScannersFactory
 */
public interface AnnotationScanner {

    /**
     * Scans for and returns all matching annotations on the selected element.
     * @param annotatedElement The {@link AnnotatedElement} (for example class or method) to scan.
     * @param annotationToFind The annotation to find.
     * @return List with all the scanned annotation on {@code annotatedElement} that match {@code annotationToFind}.
     * @param <A> Type of the annotation to find.
     */
    <A extends Annotation> List<A> scan(AnnotatedElement annotatedElement, Class<A> annotationToFind);

    /**
     * Scans and returns one matching annotation on the selected element.
     * @param annotatedElement The {@link AnnotatedElement} (for example class or method) to scan.
     * @param annotationToFind The annotation to find.
     * @return {@link Optional} with the matched annotation.
     * @param <A> Type of the annotation to find.
     */
    <A extends Annotation> Optional<A> scanOne(AnnotatedElement annotatedElement, Class<A> annotationToFind);

    /**
     * Scans and returns one matching annotation on the selected element, while throwing
     * {@link MultipleAnnotationsScannedException} if more then one match was found.
     * @param annotatedElement The {@link AnnotatedElement} (for example class or method) to scan.
     * @param annotationToFind The annotation to find.
     * @return {@link Optional} with the matched annotation.
     * @param <A> Type of the annotation to find.
     * @throws MultipleAnnotationsScannedException If more then one match was found.
     */
    <A extends Annotation> Optional<A> scanOneStrict(AnnotatedElement annotatedElement, Class<A> annotationToFind);

    /**
     * Convenience method to check only for the existence of a matched annotation.
     * @param annotatedElement The {@link AnnotatedElement} (for example class or method) to scan.
     * @param annotationToFind The annotation to find.
     * @return True only if there is a matching, scanned annotation.
     * @param <A> Type of the annotation to find.
     */
    <A extends Annotation> boolean hasAnnotation(AnnotatedElement annotatedElement, Class<A> annotationToFind);

}
