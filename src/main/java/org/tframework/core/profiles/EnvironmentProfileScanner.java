/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.utils.EnvironmentVariableNotFoundException;
import org.tframework.core.utils.EnvironmentVariableReader;

/**
 * This {@link ProfileScanner} implementation checks the system variables for profiles. The environmental
 * variable with name {@value TFRAMEWORK_PROFILES_VARIABLE_NAME} will be picked up. This variable can contain
 * a comma separated list of profiles.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE) //for testing
public class EnvironmentProfileScanner implements ProfileScanner {

	public static final String TFRAMEWORK_PROFILES_VARIABLE_NAME = "TFRAMEWORK_PROFILES";

	private final EnvironmentVariableReader environmentReader;

	/**
	 * Creates an environment profile scanner.
	 */
	public EnvironmentProfileScanner() {
		this.environmentReader = new EnvironmentVariableReader();
	}

	@Override
	public Set<String> scan() {
		try {
			String profilesRaw = environmentReader.readVariable(TFRAMEWORK_PROFILES_VARIABLE_NAME);
			log.debug("Value of '{}' environmental variable is: {}", TFRAMEWORK_PROFILES_VARIABLE_NAME, profilesRaw);

			Set<String> profiles = new HashSet<>(Arrays.asList(profilesRaw.split(",")));
			log.debug("The '{}' profile scanner will attempt to activate the following profiles: {}",
					EnvironmentProfileScanner.class.getName(), profiles);
			return profiles;
		} catch (EnvironmentVariableNotFoundException e) {
			log.debug("Environmental variable with name '{}' was not found." +
					" The '{}' profile scanner will not active any profiles.",
					EnvironmentProfileScanner.class.getName(), TFRAMEWORK_PROFILES_VARIABLE_NAME);
			return Set.of();
		}
	}
}
