/* Licensed under Apache-2.0 2022. */
package org.tframework.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The core class of the framework, which includes methods to start and stop a TFramework
 * application.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TFramework {

	/**
	 * Start a TFramework application. This is intended to be called from the {@code main} method, one time.
	 * @param args Command line arguments, as received in the {@code main} method.
	 * @return The {@link Application} that was started.
	 */
	public static Application start(String[] args) {
		return null;
	}

	/**
	 * Stops a TFramework application gracefully.
	 * @param application The {@link Application} to stop.
	 */
	public static void stop(Application application) {}
}
