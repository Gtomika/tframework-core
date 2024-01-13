/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context.assembler;

import java.lang.reflect.Method;
import org.tframework.core.di.context.ElementContext;
import org.tframework.core.di.scanner.ElementScanningResult;

/**
 * An {@link ElementContextAssembler} that assembles {@link ElementContext}s from {@link Method}s, that
 * were annotated with {@link org.tframework.core.di.annotations.Element}.
 */
public class MethodElementContextAssembler implements ElementContextAssembler<Method> {

    @Override
    public ElementContext assemble(ElementScanningResult<Method> scanningResult) {
        return null;
    }
}
