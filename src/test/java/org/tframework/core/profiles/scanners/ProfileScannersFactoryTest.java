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
package org.tframework.core.profiles.scanners;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.tframework.core.profiles.ProfileInitializationInput;

class ProfileScannersFactoryTest {

    @Test
    public void shouldCreateFrameworkProfileScanners() {
        var input = ProfileInitializationInput.builder()
                .args(new String[]{"testArg"})
                .build();
        var scanners = ProfileScannersFactory.defaultProfileScanners(input);

        assertTrue(scanners.stream().anyMatch(s -> s.getClass().equals(DefaultProfileScanner.class)));
        assertTrue(scanners.stream().anyMatch(s -> s.getClass().equals(EnvironmentProfileScanner.class)));
        assertTrue(scanners.stream().anyMatch(s -> s.getClass().equals(SystemPropertyProfileScanner.class)));
        assertTrue(scanners.stream().anyMatch(s -> s.getClass().equals(CLIProfileScanner.class)));
    }

}
