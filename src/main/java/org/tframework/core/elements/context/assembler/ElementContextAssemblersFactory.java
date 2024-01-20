/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.assembler;

import java.lang.reflect.Method;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.annotations.AnnotationScannersFactory;
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
    public static ElementContextAssembler<Class<?>> createDefaultClassElementContextAssembler() {
        return ClassElementContextAssembler.builder()
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .constructorScanner(ConstructorScannersFactory.createDefaultConstructorScanner())
                .constructorFilter(ConstructorFiltersFactory.createDefaultConstructorFilter())
                .build();
    }

    /**
     * Creates a default {@link ElementContextAssembler} for elements that are defined as {@link Method}s.
     */
    public static ElementContextAssembler<Method> createDefaultMethodElementContextAssembler() {
        return MethodElementContextAssembler.builder()
                .methodFilter(MethodFiltersFactory.createDefaultMethodFilter())
                .build();
    }


}
