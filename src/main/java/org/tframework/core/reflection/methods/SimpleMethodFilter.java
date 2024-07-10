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
