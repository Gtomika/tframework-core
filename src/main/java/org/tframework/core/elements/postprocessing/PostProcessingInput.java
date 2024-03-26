/* Licensed under Apache-2.0 2024. */
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
