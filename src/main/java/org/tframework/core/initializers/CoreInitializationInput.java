/*
Copyright 2023 Tamas Gaspar

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
package org.tframework.core.initializers;

import java.util.Set;
import lombok.Builder;
import org.tframework.core.TFrameworkInternal;
import org.tframework.core.elements.PreConstructedElementData;

/**
 * Contains the data required by the core initializer.
 * @param applicationName The name of the application.
 * @param rootClass The root class of the application.
 * @param args The command line arguments.
 * @param preConstructedElementData Collection of {@link PreConstructedElementData} with
 *                                  existing instances to add to the elements.
 * @see CoreInitializationProcess
 */
@Builder
@TFrameworkInternal
public record CoreInitializationInput(
        String applicationName,
        Class<?> rootClass,
        String[] args,
        Set<PreConstructedElementData> preConstructedElementData
) {
}
