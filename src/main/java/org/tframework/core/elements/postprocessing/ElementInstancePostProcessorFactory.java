/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.postprocessing;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Creates {@link ElementInstancePostProcessor}s and {@link ElementInstancePostProcessorAggregator}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementInstancePostProcessorFactory {

    /**
     * Creates the default {@link ElementInstancePostProcessorAggregator} of the framework.
     * @param postProcessingInput {@link PostProcessingInput} with the required data to create the post-processors.
     */
    public static ElementInstancePostProcessorAggregator createDefaultAggregator(PostProcessingInput postProcessingInput) {
        List<ElementInstancePostProcessor> processors = List.of();
        return ElementInstancePostProcessorAggregator.usingPostProcessors(processors);
    }

}
