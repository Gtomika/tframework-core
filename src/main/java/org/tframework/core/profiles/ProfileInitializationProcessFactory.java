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
package org.tframework.core.profiles;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility methods to create the {@link ProfileInitializationProcess} that the framework will use
 * during startup.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileInitializationProcessFactory {

    /**
     * Creates the {@link ProfileInitializationProcess} used during startup.
     */
    public static ProfileInitializationProcess createProfileInitializationProcess() {
        var profileCleaner = new ProfileCleaner();
        var profileValidator = new ProfileValidator();

        return new ProfileInitializationProcess(
                profileCleaner,
                profileValidator
        );
    }

}
