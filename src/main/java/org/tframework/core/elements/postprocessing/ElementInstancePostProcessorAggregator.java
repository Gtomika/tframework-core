/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.postprocessing;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.tframework.core.elements.context.ElementContext;

/**
 * Combines several {@link ElementInstancePostProcessor}s to apply all
 * necessary post-processing.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ElementInstancePostProcessorAggregator {

    private final List<ElementInstancePostProcessor> processors;

    /**
     * Perform the post-processing using all provided {@link ElementInstancePostProcessor}s.
     */
    public void postProcessInstance(ElementContext elementContext, Object instance) {
        processors.forEach(processor -> processor.postProcessInstance(elementContext, instance));
    }

    /**
     * Creates a new post-processor aggregator that will use the given post-processors.
     */
    public static ElementInstancePostProcessorAggregator usingPostProcessors(List<ElementInstancePostProcessor> processors) {
        return new ElementInstancePostProcessorAggregator(processors);
    }

}
