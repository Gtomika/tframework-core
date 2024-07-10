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
