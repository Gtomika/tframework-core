package org.tframework.core.annotations;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
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
 * scanning cannot be used to find unsupported annotations, neither will it scan unsupported annotations. The following
 * annotations are unsupported:
 * <ul>
 *     <li>Annotations in package {@code java.lang.annotation}.</li>
 * </ul>
 * The {@link #isUnsupportedAnnotation(Class)} method may be used to check if annotation is unsupported.
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
     * Scans the class (provided at construction time) for composed annotations. For details about what a composed
     * annotation is, please see the class documentation.
     * @param annotationToFind The annotation to composed scan for.
     * @return List of annotations that were found in the scan. This is a list, because a composed annotation can be present
     * multiple times on the scanned class.
     * @param <A> Type of {@code annotationToFind}.
     * @throws UnsupportedAnnotationException If {@code annotationToFind} is unsupported for composed scanning.
     */
    public <A extends Annotation> List<A> scanComposedAnnotations(Class<A> annotationToFind) {
        if(isUnsupportedAnnotation(annotationToFind)) {
            throw new UnsupportedAnnotationException(annotationToFind);
        }
        return scanComposedAnnotations(rootScannedClass, annotationToFind);
    }

    /**
     * Checks if the annotation is unsupported for composed annotation scanning. See the class documentation
     * for what types of annotations are unsupported.
     * @param annotationClass The annotation to check.
     * @return True only of the {@code annotationClass} is unsupported.
     */
    public boolean isUnsupportedAnnotation(Class<? extends Annotation> annotationClass) {
        if(UNSUPPORTED_PACKAGES.contains(annotationClass.getPackageName())) {
            return true;
        }
        return false;
    }

    private <A extends Annotation> List<A> scanComposedAnnotations(
            Class<?> scannedClass,
            Class<A> annotationToFind
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
                composedAnnotations.addAll(matchResult.matchedAnnotations());
            } else {
                //this annotation on 'scannedClass' is a different one, must check annotations on it
                var result = scanComposedAnnotations(annotationOnScannedClass.annotationType(), annotationToFind);
                composedAnnotations.addAll(result);
            }

        }
        return composedAnnotations;
    }

}
