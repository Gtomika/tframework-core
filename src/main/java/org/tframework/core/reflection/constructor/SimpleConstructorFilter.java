/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.constructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;

/**
 * A simple implementation of {@link ConstructorFilter} that uses basic reflection.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SimpleConstructorFilter implements ConstructorFilter {

    @Override
    public <A extends Annotation> Set<AnnotationFilteringResult<A, Constructor<?>>> filterByAnnotation(
            Set<Constructor<?>> constructors,
            Class<A> annotationClass,
            AnnotationScanner annotationScanner,
            boolean strict
    ) {
        return constructors.stream()
                .flatMap(constructor -> {
                    if(strict) {
                        return annotationScanner.scanOneStrict(constructor, annotationClass)
                                .map(annotation -> new AnnotationFilteringResult<A, Constructor<?>>(annotation, constructor))
                                .stream();
                    } else {
                        return annotationScanner.scanOne(constructor, annotationClass)
                                .map(annotation -> new AnnotationFilteringResult<A, Constructor<?>>(annotation, constructor))
                                .stream();
                    }
                })
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Constructor<?>> filterPublicConstructors(Set<Constructor<?>> constructors) {
        return constructors.stream()
                .filter(constructor -> constructor.getModifiers() == Modifier.PUBLIC)
                .collect(Collectors.toSet());
    }
}
