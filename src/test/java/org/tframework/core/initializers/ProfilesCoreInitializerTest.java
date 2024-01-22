/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.profiles.ProfileInitializationInput;
import org.tframework.core.profiles.ProfileInitializationProcess;
import org.tframework.core.profiles.ProfilesContainer;

@ExtendWith(MockitoExtension.class)
class ProfilesCoreInitializerTest {

    @Mock
    private ProfileInitializationProcess profileInitializationProcess;

    @Test
    public void shouldInitializeProfiles() {
        var input = ProfileInitializationInput.builder()
                .args(new String[]{"-someArg"})
                .build();
        var expectedProfiles = ProfilesContainer.fromProfiles(Set.of("a", "b"));
        when(profileInitializationProcess.initialize(input))
                .thenReturn(expectedProfiles);

        ProfilesCoreInitializer initializer = new ProfilesCoreInitializer(profileInitializationProcess);

        var actualProfiles = initializer.initialize(input);

        assertEquals(expectedProfiles, actualProfiles);
    }

}
