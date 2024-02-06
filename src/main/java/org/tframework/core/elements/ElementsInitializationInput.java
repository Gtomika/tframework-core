/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import java.util.Set;
import lombok.Builder;
import org.tframework.core.Application;

/**
 * Stores all required input for the elements initialization process.
 * @param application The not yet finalized {@link Application} instance. It should already contain the
 *                    properties and profiles.
 * @param rootClass The root class where the application was started from
 *                 (this is typically where {@link org.tframework.core.TFramework#start(String, Class, String[])}) was called from.
 * @param preConstructedElementData Collection of {@link PreConstructedElementData} with instances that should be added
 *                                  to the elements.
 * @see ElementsInitializationProcess
 */
@Builder
public record ElementsInitializationInput(
        Application application,
        Class<?> rootClass,
        Set<PreConstructedElementData> preConstructedElementData
) {
}
