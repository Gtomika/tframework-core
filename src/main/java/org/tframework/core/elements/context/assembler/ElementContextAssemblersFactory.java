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
package org.tframework.core.elements.context.assembler;

import java.lang.reflect.Method;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;
import org.tframework.core.reflection.constructor.ConstructorFiltersFactory;
import org.tframework.core.reflection.constructor.ConstructorScannersFactory;
import org.tframework.core.reflection.methods.MethodFiltersFactory;

/**
 * A factory for creating {@link ElementContextAssembler}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementContextAssemblersFactory {

    /**
     * Creates a default {@link ElementContextAssembler} for elements that are defined as {@link Class}es.
     */
    public static ClassElementContextAssembler createDefaultClassElementContextAssembler() {
        return ClassElementContextAssembler.builder()
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .constructorScanner(ConstructorScannersFactory.createDefaultConstructorScanner())
                .constructorFilter(ConstructorFiltersFactory.createDefaultConstructorFilter())
                .build();
    }

    /**
     * Creates a default {@link ElementContextAssembler} for elements that are defined as {@link Method}s.
     */
    public static MethodElementContextAssembler createDefaultMethodElementContextAssembler() {
        return MethodElementContextAssembler.builder()
                .methodFilter(MethodFiltersFactory.createDefaultMethodFilter())
                .build();
    }


}
