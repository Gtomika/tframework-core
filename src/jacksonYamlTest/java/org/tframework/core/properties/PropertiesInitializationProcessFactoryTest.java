/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.tframework.core.profiles.ProfilesContainer;

class PropertiesInitializationProcessFactoryTest {

    @Test
    void shouldNotCreatePropertiesInitializationProcess_whenClasspathIsMissingDependencies() {
        var input = PropertiesInitializationInput.builder()
                .cliArgs(new String[] {"someArg"})
                .profilesContainer(ProfilesContainer.fromProfiles(Set.of("some-profile")))
                .build();

        //this test set has a YAML parser on the classpath
        var process = PropertiesInitializationProcessFactory.createProfileInitializationProcess(input);
        assertNotNull(process);
    }
}
