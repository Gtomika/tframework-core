/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class ProfileInitializationProcessFactoryTest {

    @Test
    public void shouldCreateProfileInitializationProcess() {
        var process = ProfileInitializationProcessFactory.createProfileInitializationProcess();
        assertNotNull(process);
    }

}
