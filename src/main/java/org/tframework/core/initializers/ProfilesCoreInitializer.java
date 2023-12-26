/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.TFrameworkInternal;
import org.tframework.core.profiles.ProfileInitializationInput;
import org.tframework.core.profiles.ProfileInitializationProcess;
import org.tframework.core.profiles.ProfileScannersFactory;
import org.tframework.core.profiles.Profiles;
import org.tframework.core.utils.TimerUtils;

/**
 * The profiles {@link CoreInitializer} scans for, cleans and validates profiles at application startup.
 * @see ProfileInitializationProcess
 */
@Slf4j
@TFrameworkInternal
@RequiredArgsConstructor
public class ProfilesCoreInitializer implements CoreInitializer<ProfileInitializationInput, Profiles> {

    private final ProfileInitializationProcess profileInitializationProcess;

    /**
     * Perform profile initialization.
     * @param profileInitializationInput {@link ProfileInitializationInput} data required to start the initialization.
     * @return {@link Profiles} record with the set profiles.
     */
    @Override
    public Profiles initialize(ProfileInitializationInput profileInitializationInput) {
        log.debug("Starting profiles initialization...");
        Instant start = Instant.now();

        var profileScanners = ProfileScannersFactory.tframeworkProfileScanners(profileInitializationInput);
        Profiles profiles = profileInitializationProcess.initializeProfiles(profileScanners);

        log.info("The profile initialization completed in {} ms, and found the following profiles: {}",
                TimerUtils.msBetween(start, Instant.now()), profiles.profiles());
        return profiles;
    }
}
