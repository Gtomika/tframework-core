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
