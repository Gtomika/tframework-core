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
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesInitializationInput;
import org.tframework.core.readers.ReadersFactory;
import org.tframework.core.utils.LogUtils;

/**
 * Utilities for creating {@link PropertyFileScanner}s.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyFileScannersFactory {

    /**
     * Creates a list of {@link PropertyFileScanner}s that will be used during framework initialization.
     * @param input {@link PropertiesInitializationInput} with all data required to create the scanners.
     */
    public static List<PropertyFileScanner> createTframeworkPropertyFileScanners(PropertiesInitializationInput input) {
        //the order of the scanners is important
        var scanners = List.of(
                createDefaultScanner(),
                createProfilesScanner(input.profilesContainer()),
                createSystemPropertyScanner(),
                createEnvironmentPropertyScanner(),
                createCliArgumentScanner(input.cliArgs())
        );
        log.debug("Created the following property file scanners: {}", LogUtils.objectClassNames(scanners));
        return scanners;
    }

    private static DefaultPropertyFileScanner createDefaultScanner() {
        return new DefaultPropertyFileScanner();
    }

    private static ProfilesPropertyFileScanner createProfilesScanner(ProfilesContainer profilesContainer) {
        return new ProfilesPropertyFileScanner(profilesContainer);
    }

    private static CliArgumentPropertyFileScanner createCliArgumentScanner(String[] args) {
        return new CliArgumentPropertyFileScanner(args);
    }

    private static SystemPropertyFileScanner createSystemPropertyScanner() {
        var reader = ReadersFactory.createSystemPropertyReader();
        return new SystemPropertyFileScanner(reader);
    }

    private static EnvironmentPropertyFileScanner createEnvironmentPropertyScanner() {
        var reader = ReadersFactory.createEnvironmentVariableReader();
        return new EnvironmentPropertyFileScanner(reader);
    }

}
