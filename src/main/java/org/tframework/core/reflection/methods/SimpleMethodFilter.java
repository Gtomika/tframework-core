/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;

/**
 * A default {@link MethodFilter} implementation.
 */
@Element
public class SimpleMethodFilter implements MethodFilter {

    @Override
    public <A extends Annotation> Collection<AnnotationFilteringResult<A, Method>> filterByAnnotation(
            Collection<Method> methods,
            Class<A> annotationClass,
            AnnotationScanner annotationScanner,
            boolean strict
    ) {
        return methods.stream()
                .flatMap(method -> {
                    if(strict) {
                        return annotationScanner.scanOneStrict(method, annotationClass)
                                .map(annotation -> new AnnotationFilteringResult<>(annotation, method))
                                .stream();
                    } else {
                        return annotationScanner.scanOne(method, annotationClass)
                                .map(annotation -> new AnnotationFilteringResult<>(annotation, method))
                                .stream();
                    }
                })
                .toList();
    }

    @Override
    public boolean isPublic(Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    @Override
    public boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    @Override
    public boolean isAbstract(Method method) {
        return Modifier.isAbstract(method.getModifiers());
    }

    @Override
    public boolean hasVoidReturnType(Method method) {
        return Void.TYPE.equals(method.getReturnType()) || Void.class.equals(method.getReturnType());
    }

    @Override
    public boolean hasParameters(Method method) {
        return method.getParameters().length > 0;
    }

    @Override
    public boolean hasExactlyOneParameter(Method method) {
        return method.getParameters().length == 1;
    }
}
