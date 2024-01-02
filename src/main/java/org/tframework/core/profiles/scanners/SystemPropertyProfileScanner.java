/* Licensed under Apache-2.0 2024. */
package org.tframework.core.profiles.scanners;

import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.readers.SystemPropertyNotFoundException;
import org.tframework.core.readers.SystemPropertyReader;

/**
 * A {@link ProfileScanner} implementation that scans for profiles in the system properties. The system property
 * to use is {@value #PROFILES_SYSTEM_PROPERTY}. Multiple profiles can be provided, separated by comma.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SystemPropertyProfileScanner implements ProfileScanner {

    //if this value is updated, also update it in the documentation such as README
    public static final String PROFILES_SYSTEM_PROPERTY = "tframework.profiles";

    private final SystemPropertyReader systemPropertyReader;

    @Override
    public Set<String> scan() {
        try {
            String profilesRaw = systemPropertyReader.readProperty(PROFILES_SYSTEM_PROPERTY);
            log.debug("Found '{}' system property with value '{}'.", PROFILES_SYSTEM_PROPERTY, profilesRaw);

            var profiles = Set.of(profilesRaw.split(","));
            log.debug("The '{}' profile scanner will attempt to activate the following profiles: {}",
                    SystemPropertyProfileScanner.class.getName(), profiles);
            return profiles;
        } catch (SystemPropertyNotFoundException e) {
            log.debug("System property with name '{}' was not found." +
                    " The '{}' profile scanner will not active any profiles.",
                    PROFILES_SYSTEM_PROPERTY, SystemPropertyProfileScanner.class.getName());
            return Set.of();
        }
    }
}
