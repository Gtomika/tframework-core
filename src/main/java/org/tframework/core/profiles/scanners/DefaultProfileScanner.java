/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles.scanners;

import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This basic {@link ProfileScanner} implementation always finds only the
 * {@value DEFAULT_PROFILE_NAME} profile.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultProfileScanner implements ProfileScanner {

    public static final String DEFAULT_PROFILE_NAME = "default";

    @Override
    public Set<String> scan() {
        log.debug("The '{}' profile scanner will attempt to active the following profiles: {}",
                DefaultProfileScanner.class.getName(), DEFAULT_PROFILE_NAME);
        return Set.of(DEFAULT_PROFILE_NAME);
    }
}
