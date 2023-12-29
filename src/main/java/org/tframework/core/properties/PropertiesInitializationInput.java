/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import lombok.Builder;
import org.tframework.core.profiles.ProfilesContainer;

/**
 * Contains all required input for the {@link PropertiesInitializationProcess}.
 * @param profilesContainer {@link ProfilesContainer} with the set profiles.
 * @param cliArgs Command line arguments.
 */
@Builder
public record PropertiesInitializationInput(
        ProfilesContainer profilesContainer,
        String[] cliArgs
) {
}
