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
