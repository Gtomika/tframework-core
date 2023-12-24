/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.TFrameworkInternal;

/**
 * Utilities for creating {@link ProfileScanner}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileScanners {

	/**
	 * Creates a list of {@link ProfileScanner}s that should be used when the framework initializes.
	 * @param input {@link ProfileInitializationInput} record with all data required to construct the scanners.
	 */
	@TFrameworkInternal
	public static List<ProfileScanner> tframeworkProfileScanners(ProfileInitializationInput input) {
		return List.of(
			new DefaultProfileScanner(),
			new EnvironmentProfileScanner(),
			new CLIProfileScanner(input.args())
		);
	}

}
