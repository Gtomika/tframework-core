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
package org.tframework.core.elements.postprocessing;

import lombok.Builder;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;

/**
 * All data required for the element instance post-processing.
 * @param dependencyResolutionInput Input for post-processors which want to resolve dependencies.
 */
@Builder
public record PostProcessingInput(
        DependencyResolutionInput dependencyResolutionInput
) {
}
