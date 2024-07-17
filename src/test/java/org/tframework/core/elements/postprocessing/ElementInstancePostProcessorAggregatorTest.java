/*
Copyright 2024 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core.elements.postprocessing;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ElementInstancePostProcessorAggregatorTest extends PostProcessorBaseTest {

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

        aggregator.postProcessInstance(application, elementContext, instance);

        verify(processor, times(1)).postProcessInstance(application, elementContext, instance);
    }
}
