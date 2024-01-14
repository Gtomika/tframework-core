/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.constructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.annotations.AnnotationScanner;
import org.tframework.core.reflection.AnnotationFilteringResult;

/**
 * A simple implementation of {@link ConstructorFilter} that uses basic reflection.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SimpleConstructorFilter implements ConstructorFilter {

    @Override
    public <T, A extends Annotation> Set<AnnotationFilteringResult<A, Constructor<T>>> filterByAnnotation(
            Set<Constructor<T>> constructors,
            Class<A> annotationClass,
            AnnotationScanner annotationScanner,
            boolean strict
    ) {
        return constructors.stream()
                .flatMap(constructor -> {
                    if(strict) {
                        return annotationScanner.scanOneStrict(constructor, annotationClass)
                                .map(annotation -> new AnnotationFilteringResult<>(annotation, constructor))
                                .stream();
                    } else {
                        return annotationScanner.scanOne(constructor, annotationClass)
                                .map(annotation -> new AnnotationFilteringResult<>(annotation, constructor))
                                .stream();
                    }
                })
                .collect(Collectors.toSet());
    }

    @Override
    public <T> Set<Constructor<T>> filterPublicConstructors(Set<Constructor<T>> constructors) {
        return constructors.stream()
                .filter(constructor -> constructor.getModifiers() == Modifier.PUBLIC)
                .collect(Collectors.toSet());
    }
}
