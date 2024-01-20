/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.assembler;

import java.lang.reflect.AnnotatedElement;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.scanner.ElementScanner;
import org.tframework.core.elements.scanner.ElementScanningResult;

/**
 * The element context assembler creates {@link ElementContext} from an {@link ElementScanningResult}.
 * This result is typically produced by an {@link ElementScanner}.
 * @param <T> The type of component that had the {@link Element} annotation.
 */
public interface ElementContextAssembler<T extends AnnotatedElement> {

    /**
     * Assembles an {@link ElementContext}.
     * @param scanningResult The result of scanning for the {@link Element} annotation.
     * @throws ElementContextAssemblingException If the {@link ElementContext} could not be assembled.
     */
    ElementContext assemble(ElementScanningResult<T> scanningResult) throws ElementContextAssemblingException;

}
