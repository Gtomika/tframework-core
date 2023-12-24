/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.regex.Pattern;

/**
 * The profile validator ensures all profile names match the framework rules, which are:
 * <ul>
 *     <li>Cannot be empty, or null.</li>
 *     <li>Only lower case english letters and the dash ('-') character is allowed.</li>
 * </ul>
 */
public class ProfileValidator {

	private static final Pattern PROFILE_REGEX = Pattern.compile("[a-z\\-]+");

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
