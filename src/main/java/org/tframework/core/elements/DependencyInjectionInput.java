/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import lombok.Builder;
import org.tframework.core.Application;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainer;

/**
 * Stores all required input for the dependency injection process.
 * @param application The not yet finalized {@link Application} instance. It should already contain the
 *                    properties and profiles.
 * @param rootClass The root class where the application was started from
 *                 (this is typically where {@link org.tframework.core.TFramework#start(String, Class, String[])}) was called from.
 * @see DependencyInjectionProcess
 */
@Builder
public record DependencyInjectionInput(
        Application application,
        Class<?> rootClass
) {
}
