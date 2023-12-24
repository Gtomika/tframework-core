/* Licensed under Apache-2.0 2023. */
package org.tframework.core.annotations;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

/**
 * Interface for classes that can detect ("scan") annotations on a provided class. To check if the detected
 * annotation match the annotation to scan for, implementation should use {@link AnnotationMatcher}s. All
 * implementations should be stateless.
 * @see AnnotationMatcher
 */
public interface AnnotationScanner {

    /**
     * Scans for and returns all matching annotations on the selected class.
     * @param scannedClass The class to scan.
     * @param annotationToFind The annotation to find.
     * @return List with all the scanned annotation on {@code scannedClass} that match {@code annotationToFind}.
     * @param <A> Type of the annotation to find.
     */
    <A extends Annotation> List<A> scan(Class<?> scannedClass, Class<A> annotationToFind);

    /**
     * Scans and returns one matching annotation on the selected class.
     * @param scannedClass The class to scan.
     * @param annotationToFind The annotation to find.
     * @return {@link Optional} with the matched annotation.
     * @param <A> Type of the annotation to find.
     */
    <A extends Annotation> Optional<A> scanOne(Class<?> scannedClass, Class<A> annotationToFind);

    /**
     * Scans and returns one matching annotation on the selected class, while throwing
     * {@link MultipleAnnotationsScannedException} if more then one match was found.
     * @param scannedClass The class to scan.
     * @param annotationToFind The annotation to find.
     * @return {@link Optional} with the matched annotation.
     * @param <A> Type of the annotation to find.
     * @throws MultipleAnnotationsScannedException If more then one match was found.
     */
    <A extends Annotation> Optional<A> scanOneStrict(Class<?> scannedClass, Class<A> annotationToFind);

    /**
     * Convenience method to check only for the existence of a matched annotation.
     * @param scannedClass The class to scan.
     * @param annotationToFind The annotation to find.
     * @return True only if there is a matching, scanned annotation.
     * @param <A> Type of the annotation to find.
     */
    <A extends Annotation> boolean hasAnnotation(Class<?> scannedClass, Class<A> annotationToFind);

}
