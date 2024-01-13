/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context.assembler;

import org.tframework.core.di.context.ElementContext;
import org.tframework.core.di.scanner.ElementScanningResult;

/**
 * An {@link ElementContextAssembler} that assembles {@link ElementContext}s from {@link Class}es, that
 * were annotated with {@link org.tframework.core.di.annotations.Element}.
 */
public class ClassElementContextAssembler implements ElementContextAssembler<Class<?>> {

    @Override
    public ElementContext assemble(ElementScanningResult<Class<?>> scanningResult) {
        return null;
    }
}
