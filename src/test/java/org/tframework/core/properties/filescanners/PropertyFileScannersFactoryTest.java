/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.filescanners;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesInitializationInput;

class PropertyFileScannersFactoryTest {

    @Test
    public void shouldCreateTFrameworkPropertyFileScanners() {
        var input = PropertiesInitializationInput.builder()
                .profilesContainer(ProfilesContainer.fromProfiles(Set.of("dev")))
                .cliArgs(new String[] {"--someArg=someValue"})
                .build();
        var scanners = PropertyFileScannersFactory.createTframeworkPropertyFileScanners(input);

        assertInstanceOf(DefaultPropertyFileScanner.class, scanners.get(0));
        assertInstanceOf(ProfilesPropertyFileScanner.class, scanners.get(1));
        assertInstanceOf(SystemPropertyFileScanner.class, scanners.get(2));
        assertInstanceOf(EnvironmentPropertyFileScanner.class, scanners.get(3));
        assertInstanceOf(CliArgumentPropertyFileScanner.class, scanners.get(4));
    }

}
