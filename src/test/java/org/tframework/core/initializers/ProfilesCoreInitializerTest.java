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
package org.tframework.core.initializers;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
