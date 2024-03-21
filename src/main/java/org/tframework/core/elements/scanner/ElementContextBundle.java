/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.scanner;

import java.util.List;
import lombok.Builder;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.assembler.ClassElementContextAssembler;
import org.tframework.core.elements.context.assembler.ElementContextAssembler;
import org.tframework.core.elements.context.assembler.MethodElementContextAssembler;
import org.tframework.core.elements.context.filter.ElementContextFilterAggregator;

/**
 * A collection of all kind of scanners, assemblers and filters that will be used
 * by the framework to detect, create and process {@link ElementContext}s.
 * @param elementClassScanners Scanners that will find element classes.
 * @param elementMethodScanners Scanners that will find element methods.
 * @param classElementContextAssembler An {@link ElementContextAssembler} for creating {@link ElementContext}s from classes.
 * @param methodElementContextAssembler An {@link ElementContextAssembler} for creating {@link ElementContext}s from methods.
 * @param elementContextFilterAggregator An {@link ElementContextFilterAggregator} to filter the contexts.
 */
@Builder
public record ElementContextBundle(
        List<ElementClassScanner> elementClassScanners,
        List<ElementMethodScanner> elementMethodScanners,
        ClassElementContextAssembler classElementContextAssembler,
        MethodElementContextAssembler methodElementContextAssembler,
        ElementContextFilterAggregator elementContextFilterAggregator
) {
}
