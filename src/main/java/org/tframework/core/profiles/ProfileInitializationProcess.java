/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The profile initialization process scans for profiles to set ({@link ProfileScanner}), then
 * cleans ({@link ProfileCleaner}) and validates ({@link ProfileValidator}) the results.
 * @see Profiles
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE) //for testing
public class ProfileInitializationProcess {

    private final ProfileCleaner profileCleaner;
    private final ProfileValidator profileValidator;

    public ProfileInitializationProcess() {
        this.profileCleaner = new ProfileCleaner();
        this.profileValidator = new ProfileValidator();
    }

    /**
     * Perform the initialization process that scans for, cleans and validates profiles.
     * @param profileScanners The {@link ProfileScanner}s used to detect profiles.
     * @return {@link Profiles} record with the set profiles.
     */
    public Profiles initializeProfiles(@NonNull List<ProfileScanner> profileScanners) {
        log.debug("The profile initialization process will use these profiles scanners: {}", profileScanners);
        Set<String> profiles = ProfileMerger.merging(profileScanners)
                .mergeAndStream()
                .map(profileCleaner::clean)
                .peek(profileValidator::validate)
                .collect(Collectors.toSet());
        return new Profiles(profiles);
    }

}