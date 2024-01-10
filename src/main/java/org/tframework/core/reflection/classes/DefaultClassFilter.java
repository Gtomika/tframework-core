/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.classes;

import java.lang.annotation.Annotation;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.annotations.AnnotationScanner;

/**
 * A reasonable default implementation for {@link ClassFilter}.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultClassFilter implements ClassFilter {

    @Override
    public Collection<Class<?>> filterByAnnotation(
            Collection<Class<?>> classes,
            Class<? extends Annotation> annotationClass,
            AnnotationScanner annotationScanner
    ) {
        return classes.stream()
                .filter(clazz -> annotationScanner.hasAnnotation(clazz, annotationClass))
                .toList();
    }

    @Override
    public Collection<Class<?>> filterBySuperClass(Collection<Class<?>> classes, Class<?> superClass) {
        return classes.stream()
                .filter(superClass::isAssignableFrom)
                .toList();
    }
}
