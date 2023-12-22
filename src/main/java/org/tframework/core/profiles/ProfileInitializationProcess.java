/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.utils.TimerUtils;

/**
 * The profile initialization process scans for profiles to set ({@link ProfileScanner}), then
 * cleans ({@link ProfileCleaner}) and validates ({@link ProfileValidator}) the results.
 * @see ProfileInitializationInput
 * @see Profiles
 */
@Slf4j
public class ProfileInitializationProcess {

    private final ProfileCleaner profileCleaner = new ProfileCleaner();
    private final ProfileValidator profileValidator = new ProfileValidator();

    /**
     * Perform the initialization process that scans for, cleans and validates profiles.
     * @param input {@link ProfileInitializationInput} with all data that the process needs.
     * @return {@link Profiles} record with the set profiles.
     */
    public Profiles initializeProfiles(@NonNull ProfileInitializationInput input) {
        Instant start = Instant.now();

        Set<String> profiles = ProfileMerger.merging(input.profileScanners())
                .mergeAndStream()
                .map(profileCleaner::clean)
                .peek(profileValidator::validate)
                .collect(Collectors.toSet());

        log.info("The profile initialization completed in {} ms, and found the following profiles: {}",
                TimerUtils.msBetween(start, Instant.now()), profiles);
        return new Profiles(profiles);
    }

}
