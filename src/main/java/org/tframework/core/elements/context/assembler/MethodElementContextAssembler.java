/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.assembler;

import java.lang.reflect.Method;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.context.source.MethodElementSource;
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
    public ElementContext assemble(ElementScanningResult<Method> scanningResult) throws ElementContextAssemblingException {
        Objects.requireNonNull(parentElementContext, "parentElementContext must not be null when assembling element context from method");

        Method elementMethod = scanningResult.annotationSource();
        Class<?> elementType = elementMethod.getReturnType();
        validateMethod(elementMethod, elementType);

        ElementSource elementSource = new MethodElementSource(elementMethod, parentElementContext);
        log.trace("Created element source for element method '{}': {}", LogUtils.niceMethodName(elementMethod), elementSource);

        Element elementAnnotation = scanningResult.elementAnnotation();

        ElementContext elementContext = ElementContext.from(elementAnnotation, elementType, elementSource);

        log.debug("Created element context for element method '{}' annotated with '{}'",
                LogUtils.niceMethodName(elementMethod), ElementUtils.stringifyElementAnnotation(elementAnnotation));
        return elementContext;
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
        log.trace("The element method '{}' declared in '{}' is VALID", LogUtils.niceMethodName(method), declaredIn);
    }

    @Builder
    static MethodElementContextAssembler create(MethodFilter methodFilter) {
        return new MethodElementContextAssembler(methodFilter);
    }
}
