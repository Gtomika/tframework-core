/*
Copyright 2023 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core.reflection.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.Element;

/**
 * The composed annotation scanner implements 'extended annotation detection', which is more elaborate than
 * the simple one provided by {@link AnnotatedElement#isAnnotationPresent(Class)}. {@code isAnnotationPresent} can only find
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
 * @see AnnotationMatcher
 */
@Slf4j
@Getter
@Element
@RequiredArgsConstructor
public class ComposedAnnotationScanner implements AnnotationScanner {

    /**
     * Maximum depth of recursion when scanning for composed annotations. This is a safety measure to prevent
     * infinite loops in case of circular annotations. If the depth is exceeded, the scanning will stop.
     */
    private static final int MAX_DEPTH = 10;

    private static final String MAX_DEPTH_WARNING = "Maximum depth (" +
            MAX_DEPTH + ") of recursion exceeded, stopping the composed annotation scan."
            + " This is likely due to circular annotations.";

    /**
     * A set of packages that commonly have unsupported annotations. This is no a complete list,
     * but it covers the most common cases, and it's used for performance reasons.
     * Annotations in these packages will not be scanned. But in general, and annotation that is annotated
     * with itself will also be skipped.
     * @see #isUnsupportedAnnotation(Class)
     */
    private static final Set<String> UNSUPPORTED_PACKAGES = Set.of(
            "java.lang.annotation", "kotlin.annotation", "kotlin"
    );

    private final AnnotationMatcher annotationMatcher;
    private final RepeatedAnnotationHandler repeatedAnnotationHandler;

    /**
     * Scans the {@link AnnotatedElement} for all composed annotations.
     * @param annotatedElement {@link AnnotatedElement} to scan for the annotation.
     * @return List of annotations that were found in the scan. This is a list, because a composed annotation can be present
     * multiple times on the scanned component.
     */
    @Override
    public List<? extends Annotation> scan(AnnotatedElement annotatedElement) {
        return scanAll(annotatedElement, 0);
    }

    /**
     * Scans the {@link AnnotatedElement} for composed annotations of a specific type.
     * @param annotatedElement Component to scan for the annotation.
     * @param annotationToFind The annotation to composed scan for.
     * @return List of annotations that were found in the scan. This is a list, because a composed annotation can be present
     * multiple times on the scanned component.
     * @param <A> Type of {@code annotationToFind}.
     * @throws UnsupportedAnnotationException If {@code annotationToFind} is unsupported for composed scanning.
     */
    @Override
    public <A extends Annotation> List<A> scan(AnnotatedElement annotatedElement, Class<A> annotationToFind) {
        checkIfUnsupported(annotationToFind);
        return scanByType(annotatedElement, annotationToFind, false, 0);
    }

    /**
     * Scans the {@link AnnotatedElement} for one composed annotation. Always the first matched
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
        var scannedAnnotations = scanByType(annotatedElement, annotationToFind, true, 0);
        if(!scannedAnnotations.isEmpty()) {
            return Optional.of(scannedAnnotations.getFirst());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Scans the {@link AnnotatedElement} for one composed annotation. Unlike {@link #scan(AnnotatedElement, Class)},
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
        var scannedAnnotations = scanByType(annotatedElement, annotationToFind, false, 0);
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
     *      <li>Annotations in packages {@link #UNSUPPORTED_PACKAGES}.</li>
     *      <li>Annotations that are annotated with themselves.</li>
     * </ul>
     * @param annotationClass The annotation to check.
     * @return True only of the {@code annotationClass} is unsupported.
     */
    public boolean isUnsupportedAnnotation(Class<? extends Annotation> annotationClass) {
        if(UNSUPPORTED_PACKAGES.contains(annotationClass.getPackageName())) {
            log.trace("Annotation '{}' is in an unsupported package, skipping.", annotationClass.getName());
            return true;
        }
        if(annotationClass.isAnnotationPresent(annotationClass)) {
            log.trace("Annotation '{}' is annotated with itself, skipping.", annotationClass.getName());
            return true;
        }
        return false;
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

    /**
     * Internal method that recursively scans for <b>all</b> annotations.
     */
    private List<Annotation> scanAll(AnnotatedElement annotatedElement, int depth) {
        if(depth > MAX_DEPTH) {
            log.warn(MAX_DEPTH_WARNING);
            return new LinkedList<>();
        }
        log.trace("Scanning for ALL composed annotations on '{}'.", annotatedElement);
        List<Annotation> annotations = new LinkedList<>();
        for(Annotation annotationOnScannedComponent: annotatedElement.getAnnotations()) {
            if(isUnsupportedAnnotation(annotationOnScannedComponent.annotationType())) {
                continue;
            }
            try {
                var repeatedAnnotations = repeatedAnnotationHandler.extractRepeatedAnnotations(
                        //we simply don't know the type of the repeated annotation, but it is guaranteed to be an annotation
                        annotationOnScannedComponent, null
                );
                annotations.addAll(repeatedAnnotations);
            } catch (UnsupportedAnnotationException e) {
                //this annotation is not a container for repeated annotations, just add it
                annotations.add(annotationOnScannedComponent);
            }
            var recursiveResults = scanAll(annotationOnScannedComponent.annotationType(), depth + 1);
            annotations.addAll(recursiveResults);
        }
        return annotations;
    }

    /**
     * Internal method that recursively scans for annotations of a given type.
     * TODO: should this be rewritten to use scanAll and filter?
     */
    private <A extends Annotation> List<A> scanByType(
            AnnotatedElement annotatedElement,
            Class<A> annotationToFind,
            boolean stopOnFirstFind,
            int depth
    ) {
        if(depth > MAX_DEPTH) {
            log.warn(MAX_DEPTH_WARNING);
            return new LinkedList<>();
        }
        log.trace("Scanning for composed annotation '{}' on '{}'.", annotationToFind.getName(), annotatedElement);
        List<A> composedAnnotations = new LinkedList<>();

        for(Annotation annotationOnScannedComponent: annotatedElement.getAnnotations()) {
            //all unsupported annotations will be skipped
            if(isUnsupportedAnnotation(annotationOnScannedComponent.annotationType())) {
                continue;
            }
            var matchResult = annotationMatcher.matches(annotationToFind, annotationOnScannedComponent);
            if(matchResult.matches()) {
                //this annotation is what is scanned for, directly present on scanned component one or more times
                var matchedAnnotations = matchResult.matchedAnnotations();
                if(stopOnFirstFind && !matchedAnnotations.isEmpty()) {
                    composedAnnotations.add(matchedAnnotations.getFirst());
                    break;
                }
                composedAnnotations.addAll(matchedAnnotations);
            } else {
                //this annotation on scanned component is a different one, must check annotations on it
                var result = scanByType(
                        annotationOnScannedComponent.annotationType(),
                        annotationToFind,
                        stopOnFirstFind,
                        depth + 1
                );
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
