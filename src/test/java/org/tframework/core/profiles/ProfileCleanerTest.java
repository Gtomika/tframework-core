/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProfileCleanerTest {

    private final ProfileCleaner profileCleaner = new ProfileCleaner();

    @Test
    public void shouldCleanProfile() {
        String cleanedProfile = profileCleaner.clean(" P1   ");
        assertEquals("p1", cleanedProfile);
    }

}
