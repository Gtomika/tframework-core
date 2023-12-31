/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.TFrameworkInternal;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.PropertiesInitializationInput;
import org.tframework.core.properties.PropertiesInitializationProcess;
import org.tframework.core.properties.scanners.PropertyFileScannersFactory;
import org.tframework.core.utils.TimerUtils;

/**
 * The {@link CoreInitializer} that is responsible for loading the properties.
 * @see PropertiesInitializationProcess
 */
@Slf4j
@TFrameworkInternal
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PropertiesCoreInitializer implements CoreInitializer<PropertiesInitializationInput, PropertiesContainer> {

    private final PropertiesInitializationProcess propertiesInitializationProcess;

    @Override
    public PropertiesContainer initialize(PropertiesInitializationInput input) {
        log.info("Starting properties initialization...");
        Instant start = Instant.now();

        var propertyFileScanners = PropertyFileScannersFactory.createTframeworkPropertyFileScanners(input);
        var propertiesContainer = propertiesInitializationProcess.initialize(propertyFileScanners);

        log.info("The properties initialization completed in {} ms, and found {} properties.",
                TimerUtils.msBetween(start, Instant.now()), propertiesContainer.size());
        log.debug("The result of the properties initialization:\n{}", propertiesContainer);
        return propertiesContainer;
    }
}
