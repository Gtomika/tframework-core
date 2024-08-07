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
import java.lang.annotation.Repeatable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.Element;

/**
 * This class provides useful methods for dealing with {@link Repeatable} annotations.
 */
@Slf4j
@Element
public class RepeatedAnnotationHandler {

    /**
     * Extracts the {@link Repeatable} version of the provided annotation class.
     * If the provided annotation class is not repeatable, an empty optional is returned.
     * @param annotation The annotation class to extract the repeatable version from.
     * @return An {@link Optional} containing the repeatable version of the provided annotation class.
     */
    public Optional<Class<?>> extractRepeatableVersion(Class<?> annotation) {
        if(annotation.isAnnotationPresent(Repeatable.class)) {
            Repeatable repeatable = annotation.getAnnotation(Repeatable.class);
            return Optional.of(repeatable.value());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Extracts the repeated annotations from a {@link Repeatable} container annotation.
     * @param repeatableAnnotationContainer The annotation that is a container for the repeated annotations.
     * @param expectedAnnotationClass The expected annotation class that is repeated.
     * @return A list of the repeated annotations.
     * @throws UnsupportedAnnotationException If the provided annotation is not a container for the expected annotation.
     * @param <A> The type of the expected annotation.
     */
    @SuppressWarnings("unchecked")
    public <A extends Annotation> List<A> extractRepeatedAnnotations(
            Annotation repeatableAnnotationContainer,
            //this parameter is not used, but it is necessary to keep the generic type
            @SuppressWarnings("unused") Class<A> expectedAnnotationClass
    ) {
        //we know that 'repeatableAnnotationContainer' is containing annotation for @Repeatable of type 'expectedAnnotationClass'
        //this means that it is guaranteed to have a field 'value'
        //the 'value' field is going to contain the repeated annotation with type 'expectedAnnotationClass'.

        try {
            //if 'repeatableAnnotationContainer' is indeed a @Repeatable container for 'expectedAnnotationClass', this invocation and cast are safe
            Method containerValueMethod = repeatableAnnotationContainer.annotationType().getMethod("value");
            A[] containedAnnotations = (A[]) containerValueMethod.invoke(repeatableAnnotationContainer);
            return Arrays.asList(containedAnnotations);
        } catch (Exception e) {
            throw new UnsupportedAnnotationException(repeatableAnnotationContainer.annotationType());
        }
    }
}
