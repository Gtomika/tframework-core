/*
Copyright 2024 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
