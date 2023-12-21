/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.Test;

class ProfileCleanerTest {

    private final ProfileCleaner profileCleaner = new ProfileCleaner();

    @Test
    public void shouldCleanProfile() {
        String cleanedProfile = profileCleaner.clean(" P1   ");
        assertEquals("p1", cleanedProfile);
    }

    @Test
    public void shouldCleanProfiles() {
        var cleanedProfiles = profileCleaner.cleanAll(Set.of(" P1 ", "     P2 "));
        assertEquals(Set.of("p1", "p2"), cleanedProfiles);
    }

}