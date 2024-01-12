/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.annotations.AnnotationScanner;

/**
 * A default {@link MethodFilter} implementation.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SimpleMethodFilter implements MethodFilter {

    @Override
    public Set<Method> filterByAnnotation(
            Set<Method> methods,
            Class<? extends Annotation> annotationClass,
            AnnotationScanner annotationScanner
    ) {
        return methods.stream()
                .filter(method -> annotationScanner.hasAnnotation(method, annotationClass))
                .collect(Collectors.toSet());
    }
}
