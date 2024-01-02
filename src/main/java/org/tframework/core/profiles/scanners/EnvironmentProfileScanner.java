/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles.scanners;

import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.readers.EnvironmentVariableNotFoundException;
import org.tframework.core.readers.EnvironmentVariableReader;

/**
 * This {@link ProfileScanner} implementation checks the system variables for profiles. The environmental
 * variable with name {@value TFRAMEWORK_PROFILES_VARIABLE_NAME} will be picked up. This variable can contain
 * a comma separated list of profiles.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EnvironmentProfileScanner implements ProfileScanner {

    //if this value is updated, also update it in the documentation such as README
    public static final String TFRAMEWORK_PROFILES_VARIABLE_NAME = "TFRAMEWORK_PROFILES";

    private final EnvironmentVariableReader environmentReader;

    @Override
    public Set<String> scan() {
        try {
            String profilesRaw = environmentReader.readVariable(TFRAMEWORK_PROFILES_VARIABLE_NAME);
            log.debug("Value of '{}' environmental variable is: {}", TFRAMEWORK_PROFILES_VARIABLE_NAME, profilesRaw);

            Set<String> profiles = Set.of(profilesRaw.split(","));
            log.debug("The '{}' profile scanner will attempt to activate the following profiles: {}",
                    EnvironmentProfileScanner.class.getName(), profiles);
            return profiles;
        } catch (EnvironmentVariableNotFoundException e) {
            log.debug("Environmental variable with name '{}' was not found." +
                    " The '{}' profile scanner will not active any profiles.",
                    TFRAMEWORK_PROFILES_VARIABLE_NAME, EnvironmentProfileScanner.class.getName());
            return Set.of();
        }
    }
}
