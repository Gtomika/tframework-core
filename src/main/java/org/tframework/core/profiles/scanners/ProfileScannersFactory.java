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

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.profiles.ProfileInitializationInput;
import org.tframework.core.readers.ReadersFactory;

/**
 * Utilities for creating {@link ProfileScanner}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileScannersFactory {

    /**
     * Creates a {@link List} of {@link ProfileScanner}s that are used by the framework to detect profiles
     * during initialization.
     * @param input {@link ProfileInitializationInput} required to create these scanners.
     */
    public static List<ProfileScanner> defaultProfileScanners(ProfileInitializationInput input) {
        return List.of(
                createDefaultProfileScanner(),
                createEnvironmentProfileScanner(),
                createSystemPropertyProfileScanner(),
                createCliProfileScanner(input.args())
        );
    }

    private static EnvironmentProfileScanner createEnvironmentProfileScanner() {
        var environmentVariableReader = ReadersFactory.createEnvironmentVariableReader();
        return new EnvironmentProfileScanner(environmentVariableReader);
    }

    private static SystemPropertyProfileScanner createSystemPropertyProfileScanner() {
        var systemPropertyReader = ReadersFactory.createSystemPropertyReader();
        return new SystemPropertyProfileScanner(systemPropertyReader);
    }

    private static DefaultProfileScanner createDefaultProfileScanner() {
        return new DefaultProfileScanner();
    }

    private static CLIProfileScanner createCliProfileScanner(String[] args) {
        return new CLIProfileScanner(args);
    }

}
