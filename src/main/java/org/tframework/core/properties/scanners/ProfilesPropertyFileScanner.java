/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.scanners;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.profiles.ProfilesContainer;

/**
 * This {@link PropertyFileScanner} finds property files for each of the profiles set.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ProfilesPropertyFileScanner implements PropertyFileScanner {

    private static final String PROFILE_PROPERTY_FILE_NAME_TEMPLATE = "properties-%s.yaml";

    private final ProfilesContainer profilesContainer;

    @Override
    public List<String> scan() {
        return profilesContainer.profiles()
                .stream()
                .map(profile -> String.format(PROFILE_PROPERTY_FILE_NAME_TEMPLATE, profile))
                .peek(file -> log.debug("Scanning for profile property file: {}", file))
                .toList();
    }
}
