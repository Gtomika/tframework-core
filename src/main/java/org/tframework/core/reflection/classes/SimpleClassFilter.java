/*
Copyright 2023 Tamas Gaspar

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
package org.tframework.core.reflection.classes;

import java.lang.annotation.Annotation;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.reflection.AnnotationFilteringResult;
import org.tframework.core.reflection.annotations.AnnotationScanner;

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
