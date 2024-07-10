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

import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The profile validator ensures all profile names match the framework rules, which are:
 * <ul>
 *     <li>Cannot be empty, or null.</li>
 *     <li>Only english letters, digits, the dash ('-') or the underscore character are allowed.</li>
 *     <li>Must be at most {@value MAX_PROFILE_LENGTH} characters long.</li>
 * </ul>
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ProfileValidator {

    public static final int MAX_PROFILE_LENGTH = 50;

    private static final Pattern PROFILE_REGEX = Pattern.compile("[a-zA-Z0-9\\-_]{1,%d}".formatted(MAX_PROFILE_LENGTH));

    /**
     * Validates a profile according to the rules defined in {@link ProfileValidator} documentation.
     * @throws InvalidProfileException If this profile is not valid.
     */
    public void validate(String profile) {
        if(profile == null) {
            throw new InvalidProfileException("null");
        }
        if(!PROFILE_REGEX.matcher(profile).matches()) {
            throw new InvalidProfileException(profile);
        }
    }

}
