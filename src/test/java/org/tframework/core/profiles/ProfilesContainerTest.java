/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfilesContainerTest {

    private static final Set<String> TEST_PROFILES = Set.of("a", "b");

    private ProfilesContainer container;

    @BeforeEach
    public void setUp() {
        container = ProfilesContainer.fromProfiles(TEST_PROFILES);
    }

    @Test
    public void shouldGetCopyOfProfiles() {
        assertThrows(UnsupportedOperationException.class, () -> {
            container.profiles().remove("a"); //will have no effect on stored profiles
        });
        assertEquals(TEST_PROFILES, container.profiles());
    }

    @Test
    public void shouldCheckIfProfileIsSet() {
        assertTrue(container.isProfileSet("a"));
    }

    @Test
    public void shouldCheckIfProfileIsNotSet() {
        assertFalse(container.isProfileSet("c"));
    }

}
