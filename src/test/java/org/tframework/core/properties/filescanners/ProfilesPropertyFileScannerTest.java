/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.filescanners;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.tframework.core.profiles.ProfilesContainer;

class ProfilesPropertyFileScannerTest {

    @Test
    public void shouldScanForProfilePropertyFiles() {
        ProfilesContainer profilesContainer = ProfilesContainer.fromProfiles(Set.of("dev", "test"));
        ProfilesPropertyFileScanner scanner = new ProfilesPropertyFileScanner(profilesContainer);
        List<String> propertyFiles = scanner.scan();
        assertTrue(propertyFiles.contains("properties-dev.yaml"));
        assertTrue(propertyFiles.contains("properties-test.yaml"));
    }

}
