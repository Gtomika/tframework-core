/* Licensed under Apache-2.0 2023. */
package org.tframework.core;

import lombok.Builder;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainer;

/**
 * A bundle of information about a TFramework application.
 * @param profilesContainer {@link ProfilesContainer} that stores profiles information.
 * @param propertiesContainer {@link PropertiesContainer} that stores the properties.
 */
@Builder
public record Application(
        ProfilesContainer profilesContainer,
        PropertiesContainer propertiesContainer
) {
}
