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
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.ElementContextFactory;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.context.source.MethodElementSource;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;
import org.tframework.core.elements.scanner.ElementScanningResult;
import org.tframework.core.reflection.methods.MethodFilter;
import org.tframework.core.utils.LogUtils;

/**
 * An {@link ElementContextAssembler} that assembles {@link ElementContext}s from {@link Method}s, that
 * were annotated with {@link org.tframework.core.elements.annotations.Element}. The method must be valid for element
 * construction, for criteria see {@link #validateMethod(Method, Class)}.
 * <p>
 * This class is <b>not thread-safe</b>!
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodElementContextAssembler implements ElementContextAssembler<Method> {

    static final String DECLARED_AS = "method";
    static final String NO_RETURN_TYPE_ERROR = "Void return type is not allowed. The element method must return element instance(s).";
    static final String NOT_PUBLIC_ERROR = "The element method must be public.";
    static final String STATIC_ERROR = "The element method must be non-static.";

    private final MethodFilter methodFilter;

    @Setter
    private ElementContext parentElementContext;

    @Override
    public ElementContext assemble(
            ElementScanningResult<Method> scanningResult,
            DependencyResolutionInput dependencyResolutionInput
    ) throws ElementContextAssemblingException {
        var elementMethod = scanningResult.annotationSource();
        var elementAnnotation = scanningResult.elementAnnotation();
        log.debug("Created element context for element method '{}' annotated with '{}'",
                LogUtils.niceExecutableName(elementMethod), ElementUtils.stringifyElementAnnotation(elementAnnotation));
        return assemble(
                elementAnnotation.name(),
                elementAnnotation.scope(),
                elementMethod,
                dependencyResolutionInput
        );
    }

    private ElementContext assemble(
            String elementName,
            ElementScope elementScope,
            Method elementMethod,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        Objects.requireNonNull(parentElementContext, "parentElementContext must not be null when assembling element context from method");

        Class<?> elementType = elementMethod.getReturnType();
        validateMethod(elementMethod, elementType);

        ElementSource elementSource = new MethodElementSource(elementMethod, parentElementContext);
        log.trace("Created element source for element method '{}': {}", LogUtils.niceExecutableName(elementMethod), elementSource);

        return ElementContextFactory.from(
                elementName, elementScope, elementType, elementSource, dependencyResolutionInput
        );
    }

    /**
     * Validates the given method for element construction.
     * <ul>
     *     <li>The method must be public (to be able to invoke it).</li>
     *     <li>The method must be non-static (because it will be called as an instance method of the parent element).</li>
     *     <li>The method must have not void return type (because the returned value will be the element instance).</li>
     * </ul>
     */
    private void validateMethod(Method method, Class<?> elementType) {
        String declaredIn = parentElementContext.getType().getName();
        if(!methodFilter.isPublic(method)) {
            throw new ElementContextAssemblingException(elementType, DECLARED_AS, declaredIn, NOT_PUBLIC_ERROR);
        }
        if(methodFilter.isStatic(method)) {
            throw new ElementContextAssemblingException(elementType, DECLARED_AS, declaredIn, STATIC_ERROR);
        }
        if(methodFilter.hasVoidReturnType(method)) {
            throw new ElementContextAssemblingException(elementType, DECLARED_AS, declaredIn, NO_RETURN_TYPE_ERROR);
        }
        log.trace("The element method '{}' declared in '{}' is VALID", LogUtils.niceExecutableName(method), declaredIn);
    }

    @Builder
    static MethodElementContextAssembler create(MethodFilter methodFilter) {
        return new MethodElementContextAssembler(methodFilter);
    }
}
