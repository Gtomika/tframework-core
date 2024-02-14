/* Licensed under Apache-2.0 2024. */
package org.tframework.core.profiles.scanners;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.readers.SystemPropertyReader;

/**
 * A {@link ProfileScanner} implementation that scans for profiles in the system properties. The system property
 * to use is {@value #PROFILES_SYSTEM_PROPERTY} (other system properties where the name starts with this prefix
 * will also be picked up). Multiple profiles can be provided in a single property, separated by comma.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SystemPropertyProfileScanner implements ProfileScanner {

    //if this value is updated, also update it in the documentation such as README
    public static final String PROFILES_SYSTEM_PROPERTY = "tframework.profiles";

    private final SystemPropertyReader systemPropertyReader;

    @Override
    public Set<String> scan() {
        return systemPropertyReader.getAllSystemPropertyNames().stream()
                .filter(systemPropertyName -> systemPropertyName.startsWith(PROFILES_SYSTEM_PROPERTY))
                .flatMap(this::extractProfilesFromSystemProperty)
                .collect(Collectors.toSet());
    }

    private Stream<String> extractProfilesFromSystemProperty(String systemPropertyName) {
        String profilesRaw = systemPropertyReader.readSystemProperty(systemPropertyName);
        return Arrays.stream(profilesRaw.split(","))
                    .map(String::strip)
                    .peek(profile -> log.debug("The system property '{}' will attempt to activate the following profile: {}",
                                systemPropertyName, profile));
    }
}
