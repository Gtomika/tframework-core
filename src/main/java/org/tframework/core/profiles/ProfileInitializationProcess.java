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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.profiles.scanners.ProfileScanner;
import org.tframework.core.profiles.scanners.ProfileScannersFactory;
import org.tframework.core.utils.LogUtils;

/**
 * The profile initialization process scans for profiles to set ({@link ProfileScanner}), then
 * cleans ({@link ProfileCleaner}) and validates ({@link ProfileValidator}) the results.
 * @see ProfilesContainer
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ProfileInitializationProcess {

    private final ProfileCleaner profileCleaner;
    private final ProfileValidator profileValidator;

    /**
     * Perform the initialization process that scans for, cleans and validates profiles.
     * @param input The {@link ProfileInitializationInput} with the input parameters.
     * @return {@link ProfilesContainer} record with the set profiles.
     */
    public ProfilesContainer initialize(@NonNull ProfileInitializationInput input) {
        var profileScanners = ProfileScannersFactory.defaultProfileScanners(input);
        return initializeProfiles(profileScanners);
    }

    /**
     * Perform the initialization process that scans for, cleans and validates profiles. This
     * may be used by tests to provide mocked scanners.
     * @param profileScanners The {@link ProfileScanner}s used to detect profiles.
     * @return {@link ProfilesContainer} record with the set profiles.
     */
    ProfilesContainer initializeProfiles(@NonNull List<ProfileScanner> profileScanners) {
        log.debug("The profile initialization process will use these profiles scanners: {}",
                LogUtils.objectClassNames(profileScanners));
        Set<String> profiles = ProfileMerger.merging(profileScanners)
                .mergeAndStream()
                .map(profileCleaner::clean)
                .peek(profileValidator::validate)
                .collect(Collectors.toSet());
        return ProfilesContainer.fromProfiles(profiles);
    }

}
