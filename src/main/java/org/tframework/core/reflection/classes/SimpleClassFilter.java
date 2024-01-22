/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.classes;

import java.lang.annotation.Annotation;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.AnnotationFilteringResult;

/**
 * A reasonable default implementation for {@link ClassFilter}.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SimpleClassFilter implements ClassFilter {

    @Override
    public <A extends Annotation> Collection<AnnotationFilteringResult<A, Class<?>>> filterByAnnotation(
            Collection<Class<?>> classes,
            Class<A> annotationClass,
            AnnotationScanner annotationScanner,
            boolean strict
    ) {
        return classes.stream()
                .flatMap(clazz -> {
                    if(strict) {
                        return annotationScanner.scanOneStrict(clazz, annotationClass)
                                .map(annotation -> new AnnotationFilteringResult<A, Class<?>>(annotation, clazz))
                                .stream();
                    } else {
                        return annotationScanner.scanOne(clazz, annotationClass)
                                .map(annotation -> new AnnotationFilteringResult<A, Class<?>>(annotation, clazz))
                                .stream();
                    }
                })
                .toList();
    }

    @Override
    public Collection<Class<?>> filterBySuperClass(Collection<Class<?>> classes, Class<?> superClass) {
        return classes.stream()
                .filter(superClass::isAssignableFrom)
                .toList();
    }
}
