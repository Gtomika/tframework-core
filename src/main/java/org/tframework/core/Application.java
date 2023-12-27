/* Licensed under Apache-2.0 2023. */
package org.tframework.core;

import lombok.Builder;
import org.tframework.core.profiles.ProfilesContainer;

/**
 * A bundle of information about a TFramework application.
 * @param profilesContainer {@link ProfilesContainer} that are set.
 */
@Builder
public record Application(
        ProfilesContainer profilesContainer
) {
}
