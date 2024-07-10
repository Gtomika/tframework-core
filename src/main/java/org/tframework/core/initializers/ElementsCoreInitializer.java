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
package org.tframework.core.initializers;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.TFrameworkInternal;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.ElementsInitializationInput;
import org.tframework.core.elements.ElementsInitializationProcess;
import org.tframework.core.elements.ElementsInitializationProcessFactory;
import org.tframework.core.utils.TimerUtils;

/**
 * A {@link CoreInitializer} that loads the elements (and performs dependency injection). All work is delegated to
 * {@link ElementsInitializationProcess}.
 */
@Slf4j
@TFrameworkInternal
@RequiredArgsConstructor
public class ElementsCoreInitializer implements CoreInitializer<ElementsInitializationInput, ElementsContainer> {

    private final ElementsInitializationProcess elementsInitializationProcess;

    @Override
    public ElementsContainer initialize(ElementsInitializationInput elementsInitializationInput) {
        log.debug("Starting elements core initialization...");
        Instant start = Instant.now();

        var elementContextBundle = ElementsInitializationProcessFactory.createDefaultElementContextBundle(elementsInitializationInput);
        ElementsContainer elementsContainer = elementsInitializationProcess.initialize(elementsInitializationInput, elementContextBundle);

        log.info("The elements core initialization completed in {} ms, and found {} elements.",
                TimerUtils.msBetween(start, Instant.now()), elementsContainer.elementCount());
        return elementsContainer;
    }
}
