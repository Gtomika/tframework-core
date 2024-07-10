/*
Copyright 2024 Tamas Gaspar

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
