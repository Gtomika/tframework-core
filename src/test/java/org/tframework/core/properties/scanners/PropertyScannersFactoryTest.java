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
