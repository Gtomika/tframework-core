/* Licensed under Apache-2.0 2024. */
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
