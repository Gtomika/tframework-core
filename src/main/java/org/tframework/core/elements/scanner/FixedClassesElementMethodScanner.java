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
package org.tframework.core.elements.scanner;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.methods.MethodFilter;
import org.tframework.core.reflection.methods.MethodScanner;

/**
 * A {@link ElementScanner} that is able to find {@link Element}s
 * from the methods a fixed collection classes, provided at construction time.
 */
@Builder
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FixedClassesElementMethodScanner extends ElementMethodScanner {

    private final MethodScanner methodScanner;
    private final MethodFilter methodFilter;
    private final AnnotationScanner annotationScanner;

    @Override
    public Set<ElementScanningResult<Method>> scanElements() {
        var methods = methodScanner.scanMethods(classToScan);

        //strict filtering: if a method has more than one @Element annotation, throw an exception
        return methodFilter.filterByAnnotation(methods, Element.class, annotationScanner, true)
                .stream()
                .map(result -> new ElementScanningResult<>(result.annotation(), result.annotationSource()))
                .collect(Collectors.toSet());
    }
}
