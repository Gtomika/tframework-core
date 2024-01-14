/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context.assembler;

import java.lang.reflect.AnnotatedElement;
import org.tframework.core.di.annotations.Element;
import org.tframework.core.di.context.ElementContext;
import org.tframework.core.di.scanner.ElementScanner;
import org.tframework.core.di.scanner.ElementScanningResult;

/**
 * The element context assembler creates {@link ElementContext} from an {@link ElementScanningResult}.
 * This result is typically produced by an {@link ElementScanner}.
 * @param <T> The type of component that had the {@link Element} annotation.
 */
public interface ElementContextAssembler<T extends AnnotatedElement> {

    /**
     * Assembles an {@link ElementContext}.
     * @param scanningResult The result of scanning for the {@link Element} annotation.
     */
    ElementContext assemble(ElementScanningResult<T> scanningResult);

}
