/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.tframework.core.elements.annotations.Element;

/**
 * The composed annotation scanner implements 'extended annotation detection', which is more elaborate than
 * the simple one provided by {@link Class#isAnnotationPresent(Class)}. {@code isAnnotationPresent} can only find
 * <b>directly present</b> annotations. On the other hand, this scanner finds <b>composed annotations.</b>
 * <br>
 * We say that a component {@code A} has composed annotation {@code @B} if either is fulfilled:
 * <ul>
 *     <li>{@code A} is directly annotated with {@code @B} (thus, this is an extension of {@link Class#isAnnotationPresent(Class)}).</li>
 *     <li>Any <b>supported</b> annotations on component {@code A} have composed annotation {@code @B} (recursion).</li>
 * </ul>
 * Note the word 'supported'. Some types of annotations are unsupported due to technical limitations. Composed annotation
 * scanning cannot be used to find unsupported annotations, neither will it scan them. See {@link #isUnsupportedAnnotation(Class)}
 * method for the types of unsupported annotations.
 * <br>
 * While annotations that are placed on themselves are supported, only the first occurrence of the annotation will be
 * found by the scanner (the one on itself will not be found). Also, for circular annotations (where 2 annotations are
 * placed on each other), the scanning will stop at the first match.
 * @see AnnotationMatcher
 */
@Getter
@Element
@RequiredArgsConstructor
public class ComposedAnnotationScanner implements AnnotationScanner {

    private static final Set<String> UNSUPPORTED_PACKAGES = Set.of("java.lang.annotation");

    private final AnnotationMatcher annotationMatcher;

    /**
     * Scans the class (provided at construction time) for composed annotations.
     * @param annotatedElement Element to scan for the annotation.
     * @param annotationToFind The annotation to composed scan for.
     * @return List of annotations that were found in the scan. This is a list, because a composed annotation can be present
     * multiple times on the scanned class.
     * @param <A> Type of {@code annotationToFind}.
     * @throws UnsupportedAnnotationException If {@code annotationToFind} is unsupported for composed scanning.
     */
    @Override
    public <A extends Annotation> List<A> scan(AnnotatedElement annotatedElement, Class<A> annotationToFind) {
        checkIfUnsupported(annotationToFind);
        return scan(annotatedElement, annotationToFind, false);
    }

    /**
     * Scans the class (provided at construction time) for one composed annotation. Always the first matched
     * annotation is returned. If only one scanned annotation is needed, this method is a more performant
     * choice than {@link #scan(AnnotatedElement, Class)}. If you need to make sure that at most one annotation was found,
     * use {@link #scanOneStrict(AnnotatedElement, Class)} instead.
     * @param annotatedElement Element to scan for the annotation.
     * @param annotationToFind The annotation to composed scan for.
     * @return {@link Optional} with the annotation if found, or empty if not found.
     * @param <A> Type of {@code annotationToFind}.
     * @throws UnsupportedAnnotationException If {@code annotationToFind} is unsupported for composed scanning.
     */
    @Override
    public <A extends Annotation> Optional<A> scanOne(AnnotatedElement annotatedElement, Class<A> annotationToFind) {
        checkIfUnsupported(annotationToFind);
        var scannedAnnotations = scan(annotatedElement, annotationToFind, true);
        if(!scannedAnnotations.isEmpty()) {
            return Optional.of(scannedAnnotations.getFirst());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Scans the class (provided at construction time) for one composed annotation. Unlike {@link #scan(AnnotatedElement, Class)},
     * this method will raise an exception if multiple annotations are found.
     * @param annotatedElement Element to scan for the annotation.
     * @param annotationToFind The annotation to composed scan for.
     * @return {@link Optional} with the annotation if found, or empty if not found.
     * @param <A> Type of {@code annotationToFind}.
     * @throws UnsupportedAnnotationException If {@code annotationToFind} is unsupported for composed scanning.
     * @throws MultipleAnnotationsScannedException If more than one annotation was found during the scan.
     */
    @Override
    public <A extends Annotation> Optional<A> scanOneStrict(AnnotatedElement annotatedElement, Class<A> annotationToFind) {
        checkIfUnsupported(annotationToFind);
        var scannedAnnotations = scan(annotatedElement, annotationToFind, false);
        if(scannedAnnotations.size() == 1) {
            return Optional.of(scannedAnnotations.getFirst());
        } else if(scannedAnnotations.size() > 1) {
            throw new MultipleAnnotationsScannedException(annotatedElement, scannedAnnotations);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Checks if the annotation is unsupported for composed annotation scanning. The following annotations are unsupported:
     * <ul>
     *      <li>Annotations in package {@code java.lang.annotation}.</li>
     * </ul>
     * @param annotationClass The annotation to check.
     * @return True only of the {@code annotationClass} is unsupported.
     */
    public boolean isUnsupportedAnnotation(Class<? extends Annotation> annotationClass) {
        return UNSUPPORTED_PACKAGES.contains(annotationClass.getPackageName());
    }

    /**
     * Checks if the class has at least one composed annotation.
     * @param annotatedElement Element to scan for the annotation.
     * @param annotationToFind The annotation to check for.
     * @return True only if at least one composed annotation was found.
     * @param <A> Type of {@code annotationToFind}.
     */
    @Override
    public <A extends Annotation> boolean hasAnnotation(AnnotatedElement annotatedElement, Class<A> annotationToFind) {
        return scanOne(annotatedElement, annotationToFind).isPresent();
    }

    private <A extends Annotation> List<A> scan(
            AnnotatedElement annotatedElement,
            Class<A> annotationToFind,
            boolean stopOnFirstFind
    ) {
        List<A> composedAnnotations = new LinkedList<>();

        for(Annotation annotationOnScannedClass: annotatedElement.getAnnotations()) {

            //all unsupported annotations will be skipped
            if(isUnsupportedAnnotation(annotationOnScannedClass.annotationType())) {
                continue;
            }

            var matchResult = annotationMatcher.matches(annotationToFind, annotationOnScannedClass);
            if(matchResult.matches()) {
                //this annotation is what is scanned for, directly present on 'scannedClass' one or more times
                var matchedAnnotations = matchResult.matchedAnnotations();
                if(stopOnFirstFind && !matchedAnnotations.isEmpty()) {
                    composedAnnotations.add(matchedAnnotations.getFirst());
                    break;
                }
                composedAnnotations.addAll(matchedAnnotations);
            } else {
                //this annotation on 'scannedClass' is a different one, must check annotations on it
                var result = scan(annotationOnScannedClass.annotationType(), annotationToFind, stopOnFirstFind);
                if(stopOnFirstFind && !result.isEmpty()) {
                    composedAnnotations.add(result.getFirst());
                    break;
                }
                composedAnnotations.addAll(result);
            }

        }
        return composedAnnotations;
    }

    private void checkIfUnsupported(Class<? extends Annotation> annotationToFind) {
        if(isUnsupportedAnnotation(annotationToFind)) {
            throw new UnsupportedAnnotationException(annotationToFind);
        }
    }

}
