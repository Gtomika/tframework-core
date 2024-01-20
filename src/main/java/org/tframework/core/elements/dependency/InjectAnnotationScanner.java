/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.tframework.core.annotations.AnnotationScanner;
import org.tframework.core.annotations.MultipleAnnotationsScannedException;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.annotations.InjectProperty;

/**
 * This class is a specialized {@link AnnotationScanner} that has useful methods for finding '@InjectX' annotations.
 * See {@link #INJECT_ANNOTATIONS} for the list of supported annotations.
 * This kind of annotation scanning is always strict, because multiple '@InjectX' annotations are not allowed on the same
 * component: it would be impossible to determine which one to use.
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class InjectAnnotationScanner {

    public static final List<Class<? extends Annotation>> INJECT_ANNOTATIONS = List.of(
            InjectElement.class,
            InjectProperty.class
    );

    private final AnnotationScanner annotationScanner;

    /**
     * Finds the only '@InjectX' annotation on the given {@link AnnotatedElement}.
     * @param annotatedElement The {@link AnnotatedElement} to scan, must not be null.
     * @param injectAnnotationType The type of the '@InjectX' annotation to find, must not be null. Should be
     *                             one of {@link #INJECT_ANNOTATIONS}.
     * @return The found annotation, or empty if none was found.
     * @param <A> The type of the '@InjectX' annotation to find.
     * @throws MultipleAnnotationsScannedException If more than one '@InjectX' annotation was found.
     */
    public <A extends Annotation> Optional<A> findInjectAnnotation(
            @NonNull AnnotatedElement annotatedElement,
            @NonNull Class<A> injectAnnotationType
    ) {
        List<Annotation> injectAnnotations = new ArrayList<>();

        for(Class<? extends Annotation> injectAnnotation : INJECT_ANNOTATIONS) {
            //strict scanning will make sure that a one type of '@InjectX' annotation is not found multiple times
            annotationScanner.scanOneStrict(annotatedElement, injectAnnotation)
                    .ifPresent(injectAnnotations::add);
        }

        if(injectAnnotations.size() > 1) {
            throw new MultipleAnnotationsScannedException(injectAnnotations);
        }

        return injectAnnotations.stream()
                .flatMap(injectAnnotation -> {
                    try {
                        return Stream.of(injectAnnotationType.cast(injectAnnotation));
                    } catch (ClassCastException e) {
                        //the one found annotation was not of the right type
                        return Stream.empty();
                    }
                })
                .findFirst();
    }
}
