/* Licensed under Apache-2.0 2023. */
package org.tframework.core;

import lombok.Builder;
import org.tframework.core.profiles.Profiles;

/**
 * A bundle of information about a TFramework application.
 * @param profiles {@link Profiles} that are set.
 */
@Builder
public record Application(
		Profiles profiles
) {
}
