/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.filescanners;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.profiles.ProfilesContainer;

/**
 * This {@link PropertyFileScanner} finds property files for each of the profiles set. The files must
 * be in the resources folder and have name in this format: {@value PROFILE_PROPERTY_FILE_NAME_TEMPLATE},
 * where {@code {profile}} is replaced with the profile name. It is not required to have these files for
 * the profiles.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ProfilesPropertyFileScanner implements PropertyFileScanner {

    private static final String PROFILE_PROPERTY_FILE_NAME_TEMPLATE = "properties-{profile}.yaml";

    private final ProfilesContainer profilesContainer;

    @Override
    public List<String> scan() {
        return profilesContainer.profiles()
                .stream()
                .map(profile -> PROFILE_PROPERTY_FILE_NAME_TEMPLATE.replaceFirst("\\{profile}", profile))
                .peek(file -> log.debug("Adding property file activated by profile: {}", file))
                .toList();
    }

    @Override
    public String sourceName() {
        return "Profiles (for each profile one file: " + PROFILE_PROPERTY_FILE_NAME_TEMPLATE + ")";
    }
}
