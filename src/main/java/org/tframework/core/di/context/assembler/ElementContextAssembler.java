/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context.assembler;

import org.tframework.core.di.context.ElementContext;

/**
 * The element context assembler creates {@link ElementContext} from some input.
 * The input can be a class, a method, for example, and it is up to the implementation.
 */
public interface ElementContextAssembler {

    /**
     * Assembles an {@link ElementContext}.
     */
    ElementContext assemble();

}
