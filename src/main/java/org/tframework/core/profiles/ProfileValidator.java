/* Licensed under Apache-2.0 2023. */
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
