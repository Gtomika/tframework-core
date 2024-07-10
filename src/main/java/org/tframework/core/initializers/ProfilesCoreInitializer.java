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

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.TFrameworkInternal;
import org.tframework.core.profiles.ProfileInitializationInput;
import org.tframework.core.profiles.ProfileInitializationProcess;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.utils.TimerUtils;

/**
 * The profiles {@link CoreInitializer} scans for, cleans and validates profiles at application startup.
 * All work is delegated to the {@link ProfileInitializationProcess}.
 */
@Slf4j
@TFrameworkInternal
@RequiredArgsConstructor
public class ProfilesCoreInitializer implements CoreInitializer<ProfileInitializationInput, ProfilesContainer> {

    private final ProfileInitializationProcess profileInitializationProcess;

    /**
     * Perform profile initialization.
     * @param profileInitializationInput {@link ProfileInitializationInput} data required to start the initialization.
     * @return {@link ProfilesContainer} record with the set profiles.
     */
    @Override
    public ProfilesContainer initialize(ProfileInitializationInput profileInitializationInput) {
        log.debug("Starting profiles initialization...");
        Instant start = Instant.now();

        ProfilesContainer profilesContainer = profileInitializationProcess.initialize(profileInitializationInput);

        log.info("The profile initialization completed in {} ms, and found the following profiles: {}",
                TimerUtils.msBetween(start, Instant.now()), profilesContainer.profiles());
        return profilesContainer;
    }
}
