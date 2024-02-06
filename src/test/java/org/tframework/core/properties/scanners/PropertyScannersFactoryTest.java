/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.scanners;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesInitializationInput;

class PropertyScannersFactoryTest {

    @Test
    public void shouldCreateDefaultPropertyScanners() {
        var input = PropertiesInitializationInput.builder()
                .cliArgs(new String[] {"someArg"})
                .profilesContainer(ProfilesContainer.empty())
                .build();

        var scanners = PropertyScannersFactory.createDefaultPropertyScanners(input);

        assertTrue(scanners.stream().anyMatch(s -> s instanceof CliArgumentPropertyScanner));
        assertTrue(scanners.stream().anyMatch(s -> s instanceof EnvironmentPropertyScanner));
        assertTrue(scanners.stream().anyMatch(s -> s instanceof SystemPropertyScanner));
    }

}
