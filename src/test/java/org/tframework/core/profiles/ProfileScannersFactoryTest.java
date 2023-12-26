/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ProfileScannersFactoryTest {

    @Test
    public void shouldCreateFrameworkProfileScanners() {
        var input = ProfileInitializationInput.builder()
                .args(new String[]{"testArg"})
                .build();
        var scanners = ProfileScannersFactory.tframeworkProfileScanners(input);

        assertTrue(scanners.stream().anyMatch(s -> s.getClass().equals(DefaultProfileScanner.class)));
        assertTrue(scanners.stream().anyMatch(s -> s.getClass().equals(EnvironmentProfileScanner.class)));
        assertTrue(scanners.stream().anyMatch(s -> s.getClass().equals(CLIProfileScanner.class)));
    }

}
