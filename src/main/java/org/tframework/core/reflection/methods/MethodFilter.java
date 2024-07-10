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
import java.util.Collection;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;

/**
 * A method filter selects methods based on some criteria. The methods are
 * usually found by a {@link MethodScanner} and then processed by a filter.
 */
public interface MethodFilter {

    /**
     * Filters methods by a given annotation.
     * @param methods Methods to filter.
     * @param annotationClass Annotation to filter by.
     * @param annotationScanner An {@link AnnotationScanner} to use to determine if the annotation is present.
     * @return A collection of {@link AnnotationFilteringResult}s, one for each method that was annotated.
     * @param <A> Type of annotation.
     */
    <A extends Annotation> Collection<AnnotationFilteringResult<A, Method>> filterByAnnotation(
            Collection<Method> methods,
            Class<A> annotationClass,
            AnnotationScanner annotationScanner,
            boolean strict
    );

    /**
     * Checks if the given method is public.
     */
    boolean isPublic(Method method);

    /**
     * Checks if the given method is static.
     */
    boolean isStatic(Method method);

    /**
     * Checks if the given method is abstract.
     */
    boolean isAbstract(Method method);

    /**
     * Checks if the given method has void return type. It will find both the
     * primitive {@code void} and the wrapper {@link Void} return types.
     */
    boolean hasVoidReturnType(Method method);

    /**
     * Checks if the method has any parameters.
     */
    boolean hasParameters(Method method);

    /**
     * Checks if the method has exactly one parameter.
     */
    boolean hasExactlyOneParameter(Method method);

}
