/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.assembler;

import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;
import org.tframework.core.elements.scanner.ElementScanner;
import org.tframework.core.elements.scanner.ElementScanningResult;

import java.lang.reflect.AnnotatedElement;

/**
 * The element context assembler creates {@link ElementContext} from an {@link ElementScanningResult}.
 * This result is typically produced by an {@link ElementScanner}.
 * @param <T> The type of component that had the {@link Element} annotation.
 */
public interface ElementContextAssembler<T extends AnnotatedElement> {

    /**
     * Assembles an {@link ElementContext}.
     * @param scanningResult The result of scanning for the {@link Element} annotation.
     * @param dependencyResolutionInput {@link DependencyResolutionInput} that will allow the created
     *                                  context to resolve its dependencies.
     * @throws ElementContextAssemblingException If the {@link ElementContext} could not be assembled.
     */
    ElementContext assemble(
            ElementScanningResult<T> scanningResult,
            DependencyResolutionInput dependencyResolutionInput
    ) throws ElementContextAssemblingException;

}
