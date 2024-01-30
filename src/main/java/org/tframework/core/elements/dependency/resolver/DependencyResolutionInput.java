/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import lombok.Builder;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.properties.PropertiesContainer;

/**
 * All input for creating {@link BasicDependencyResolver}s.
 */
@Builder
public record DependencyResolutionInput(
        ElementsContainer elementsContainer,
        PropertiesContainer propertiesContainer
) {
}
