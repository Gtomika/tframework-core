/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.postprocessing;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.context.ElementContext;

@ExtendWith(MockitoExtension.class)
public class ElementInstancePostProcessorAggregatorTest {

    @Mock
    private ElementContext elementContext;

    @Mock
    private ElementInstancePostProcessor processor;

    private ElementInstancePostProcessorAggregator aggregator;

    @BeforeEach
    public void setUp() {
        aggregator = ElementInstancePostProcessorAggregator.usingPostProcessors(List.of(processor));
    }

    @Test
    public void shouldPostProcessInstance() {
        Object instance = new Object();

        aggregator.postProcessInstance(elementContext, instance);

        verify(processor, times(1)).postProcessInstance(elementContext, instance);
    }
}
