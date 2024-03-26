/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.postprocessing;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;

public class ElementInstancePostProcessorFactoryTest {

    @Test
    public void shouldCreateDefaultAggregator() {
        var input = PostProcessingInput.builder()
                .dependencyResolutionInput(DependencyResolutionInput.builder().build())
                .build();
        var aggregator = ElementInstancePostProcessorFactory.createDefaultAggregator(input);

        assertTrue(aggregator.getProcessors().isEmpty());
    }
}
