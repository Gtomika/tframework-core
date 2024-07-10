/*
Copyright 2023 Tamas Gaspar

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
