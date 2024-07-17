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

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.tframework.core.Application;
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
    public void postProcessInstance(Application application, ElementContext elementContext, Object instance) {
        processors.forEach(processor -> processor.postProcessInstance(application, elementContext, instance));
    }

    /**
     * Creates a new post-processor aggregator that will use the given post-processors.
     */
    public static ElementInstancePostProcessorAggregator usingPostProcessors(List<ElementInstancePostProcessor> processors) {
        return new ElementInstancePostProcessorAggregator(processors);
    }

}
