/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles.scanners;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import org.junit.jupiter.api.Test;

class DefaultProfileScannerTest {

    @Test
    public void shouldScanDefaultProfile() {
        DefaultProfileScanner scanner = new DefaultProfileScanner();
        Set<String> profiles = scanner.scan();

        assertEquals(Set.of(DefaultProfileScanner.DEFAULT_PROFILE_NAME), profiles);
    }

}
