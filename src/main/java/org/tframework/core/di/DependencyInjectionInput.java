/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di;

import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainer;

/**
 * Stores all required input for the dependency injection process.
 * @param rootClass The root class where the application was started from
 *                 (where {@link org.tframework.core.TFramework#start(String[])}) was called from.
 * @param profilesContainer The container that stores all profiles.
 * @param propertiesContainer The container that stores all properties.
 * @see DependencyInjectionProcess
 */
public record DependencyInjectionInput(
        Class<?> rootClass,
        ProfilesContainer profilesContainer,
        PropertiesContainer propertiesContainer
) {
}
