package org.tframework.core.annotations;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The composed annotation scanner implements 'extended annotation detection', which is more elaborate than
 * the simple one provided by {@link Class#isAnnotationPresent(Class)}. {@code isAnnotationPresent} can only find
 * <b>directly present</b> annotations. On the other hand, this scanner finds <b>composed annotations.</b>
 * <br>
 * We say that class {@code A} has composed annotation {@code @B} if either is fulfilled:
 * <ul>
 *     <li>{@code A} is directly annotated with {@code @B} (thus, this is an extension of {@link Class#isAnnotationPresent(Class)}).</li>
 *     <li>Any <b>supported</b> annotations on class {@code A} have composed annotation {@code @B} (recursion).</li>
 * </ul>
 * Note the word 'supported'. Some types of annotations are unsupported due to technical limitations. Composed annotation
 * scanning cannot be used to find unsupported annotations, neither will it them. See {@link #isUnsupportedAnnotation(Class)}
 * method for the types of unsupported annotations.
 * <br>
 * While annotations that are placed on themselves are supported, only the first occurrence of the annotation will be
 * found by the scanner (the one on itself will not be found). Also, for circular annotations (where 2 annotations are
 * placed on each other), the scanning will stop at the first match.
 * @see AnnotationMatcher
 */
public class ComposedAnnotationScanner {

    private static final Set<String> UNSUPPORTED_PACKAGES = Set.of("java.lang.annotation");

    private final Class<?> rootScannedClass;

    /**
     * Create a composed annotation scanner that scans the specified class.
     * @param scannedClass Class to scan.
     */
    public ComposedAnnotationScanner(Class<?> scannedClass) {
        this.rootScannedClass = scannedClass;
    }

    /**
     * Scans the class (provided at construction time) for composed annotations.
     * @param annotationToFind The annotation to composed scan for.
     * @return List of annotations that were found in the scan. This is a list, because a composed annotation can be present
     * multiple times on the scanned class.
     * @param <A> Type of {@code annotationToFind}.
     * @throws UnsupportedAnnotationException If {@code annotationToFind} is unsupported for composed scanning.
     */
    public <A extends Annotation> List<A> scan(Class<A> annotationToFind) {
        checkIfUnsupported(annotationToFind);
        return scan(rootScannedClass, annotationToFind, false);
    }

    /**
     * Scans the class (provided at construction time) for one composed annotation. Always the first matched
     * annotation is returned. If only one scanned annotation is needed, this method is a more performant
     * choice than {@link #scan(Class)}. If you need to make sure that at most one annotation was found,
     * use {@link #scanOneStrict(Class)} instead.
     * @param annotationToFind The annotation to composed scan for.
     * @return {@link Optional} with the annotation if found, or empty if not found.
     * @param <A> <A> Type of {@code annotationToFind}.
     * @throws UnsupportedAnnotationException If {@code annotationToFind} is unsupported for composed scanning.
     */
    public <A extends Annotation> Optional<A> scanOne(Class<A> annotationToFind) {
        checkIfUnsupported(annotationToFind);
        var scannedAnnotations = scan(rootScannedClass, annotationToFind, true);
        if(!scannedAnnotations.isEmpty()) {
            return Optional.of(scannedAnnotations.getFirst());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Scans the class (provided at construction time) for one composed annotation. Unlike {@link #scan(Class)},
     * this method will raise an exception if multiple annotations are found.
     * @param annotationToFind The annotation to composed scan for.
     * @return {@link Optional} with the annotation if found, or empty if not found.
     * @param <A> <A> Type of {@code annotationToFind}.
     * @throws UnsupportedAnnotationException If {@code annotationToFind} is unsupported for composed scanning.
     * @throws MultipleAnnotationsScannedException If more than one annotation was found during the scan.
     */
    public <A extends Annotation> Optional<A> scanOneStrict(Class<A> annotationToFind) {
        checkIfUnsupported(annotationToFind);
        var scannedAnnotations = scan(rootScannedClass, annotationToFind, false);
        if(scannedAnnotations.size() == 1) {
            return Optional.of(scannedAnnotations.getFirst());
        } else if(scannedAnnotations.size() > 1) {
            throw new MultipleAnnotationsScannedException(scannedAnnotations);
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

    private <A extends Annotation> List<A> scan(
            Class<?> scannedClass,
            Class<A> annotationToFind,
            boolean stopOnFirstFind
    ) {
        List<A> composedAnnotations = new LinkedList<>();

        for(Annotation annotationOnScannedClass: scannedClass.getAnnotations()) {

            //all unsupported annotations will be skipped
            if(isUnsupportedAnnotation(annotationOnScannedClass.annotationType())) {
                continue;
            }

            var matcher = new AnnotationMatcher<>(annotationToFind);
            var matchResult = matcher.matches(annotationOnScannedClass);

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
