/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers.core;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.profiles.ProfileInitializationInput;
import org.tframework.core.profiles.ProfileInitializationProcess;
import org.tframework.core.profiles.ProfileScanners;
import org.tframework.core.profiles.Profiles;
import org.tframework.core.utils.TimerUtils;

/**
 * The profiles {@link CoreInitializer} scans for, cleans and validates profiles at application startup.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE) //for testing
public class ProfilesCoreInitializer implements CoreInitializer<ProfileInitializationInput, Profiles> {

	private final ProfileInitializationProcess profileInitializationProcess;

	/**
	 * Creates a profiles initializer that will use the default process to set profiles.
	 */
	public ProfilesCoreInitializer() {
		this.profileInitializationProcess = new ProfileInitializationProcess();
	}

	/**
	 * Perform profile initialization.
	 * @param profileInitializationInput {@link ProfileInitializationInput} data required to start the initialization.
	 * @return {@link Profiles} record with the set profiles.
	 */
	@Override
	public Profiles initialize(ProfileInitializationInput profileInitializationInput) {
		log.debug("Starting profiles initialization...");
		Instant start = Instant.now();

		var profileScanners = ProfileScanners.tframeworkProfileScanners(profileInitializationInput);
		Profiles profiles = profileInitializationProcess.initializeProfiles(profileScanners);

		log.info("The profile initialization completed in {} ms, and found the following profiles: {}",
				TimerUtils.msBetween(start, Instant.now()), profiles.profiles());
		return profiles;
	}
}
