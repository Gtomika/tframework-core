/*
Copyright 2024 Tamas Gaspar

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
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.tframework.core.utils.MultiValueMap;

/**
 * This class stores annotations by type, that were previously scanned. This is
 * useful to reduce the amount of scanning that needs to be done, and make these
 * annotations available to any component that needs them.
 */
public class PreScannedAnnotations {

    private final AnnotatedElement source;
    private final MultiValueMap<Class<? extends Annotation>, Annotation> annotations;

    PreScannedAnnotations(AnnotatedElement source, MultiValueMap<Class<? extends Annotation>, Annotation> annotations) {
        this.source = source;
        this.annotations = annotations;
    }

    PreScannedAnnotations(AnnotatedElement source) {
        this(source, new MultiValueMap<>());
    }

    /**
     * Adds an annotation to the store.
     * @param annotation The annotation to store, cannot be null.
     */
    public void add(@NonNull Annotation annotation) {
        annotations.putValue(annotation.annotationType(), annotation);
    }

    /**
     * Gets all annotations of a given type.
     * @param annotationType The type of annotation to get, cannot be null.
     * @return A list with the annotations, or an empty list if none were found.
     * @param <A> The type of annotation to get.
     */
    @SuppressWarnings("unchecked")
    public <A extends Annotation> List<A> getAnnotations(@NonNull Class<A> annotationType) {
        return (List<A>) annotations.getOrEmptyList(annotationType);
    }

    /**
     * Strictly gets an annotation of a given type, if it exists.
     * @param annotationType The type of annotation to get, cannot be null.
     * @return An optional with the annotation, or an empty optional if none were found.
     * @param <A> The type of annotation to get.
     * @throws MultipleAnnotationsScannedException If more than one annotation of the given type was found.
     */
    public <A extends Annotation> Optional<A> getAnnotationStrict(@NonNull Class<A> annotationType) {
        List<A> annotations = getAnnotations(annotationType);
        if (annotations.size() > 1) {
            throw new MultipleAnnotationsScannedException(source, annotations);
        }
        return annotations.stream().findAny();
    }

    /**
     * Checks if this store has any annotations of a given type.
     * @param annotationType The type of annotation to check for, cannot be null.
     */
    public <A extends Annotation> boolean hasAnnotation(@NonNull Class<A> annotationType) {
        return annotations.containsKey(annotationType);
    }

    /**
     * Creates a new instance of this class, without any stored annotations.
     * @param source The source {@link AnnotatedElement} of the annotations, like a class or method.
     */
    public static PreScannedAnnotations empty(AnnotatedElement source) {
        return new PreScannedAnnotations(source);
    }

    /**
     * Scans the annotations of a given source, and stores them in a new instance of this class.
     * @param source The source {@link AnnotatedElement} of the annotations, like a class or method.
     * @param scanner An {@link AnnotationScanner} to scan the annotations.
     */
    public static PreScannedAnnotations scanAndStore(AnnotatedElement source, AnnotationScanner scanner) {

    }
}
