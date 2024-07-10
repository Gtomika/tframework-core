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
