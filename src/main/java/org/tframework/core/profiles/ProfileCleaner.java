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

import java.util.Locale;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The profile cleaner is responsible for creating a unified format
 * for profiles. This includes:
 * <ul>
 *     <li>Converting letters to lower case.</li>
 *     <li>Stripping leading and trailing whitespace characters.</li>
 * </ul>
 * This class will not validate, raise exceptions or remove any profiles from its input.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ProfileCleaner {

    /**
     * Cleans the profile according to the rules specified at the class documentation.
     */
    public String clean(String profile) {
        return profile.toLowerCase(Locale.ROOT).strip();
    }

}
