package org.tframework.core.initializers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.TFrameworkInternal;
import org.tframework.core.elements.DependencyInjectionInput;
import org.tframework.core.elements.DependencyInjectionProcess;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.utils.TimerUtils;

import java.time.Instant;

/**
 * A {@link CoreInitializer} that loads the elements (and performs dependency injection). All work is delegated to
 * {@link DependencyInjectionProcess}.
 */
@Slf4j
@TFrameworkInternal
@RequiredArgsConstructor
public class DependencyInjectionCoreInitializer implements CoreInitializer<DependencyInjectionInput, ElementsContainer> {

    private final DependencyInjectionProcess dependencyInjectionProcess;

    @Override
    public ElementsContainer initialize(DependencyInjectionInput dependencyInjectionInput) {
        log.debug("Starting dependency injection initialization...");
        Instant start = Instant.now();

        ElementsContainer elementsContainer = dependencyInjectionProcess.initialize(dependencyInjectionInput);

        log.info("The dependency injection initialization completed in {} ms, and found {} elements.",
                TimerUtils.msBetween(start, Instant.now()), elementsContainer.elementCount());
        return elementsContainer;
    }
}
