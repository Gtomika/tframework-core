package org.tframework.core.elements.dependency;

import lombok.Builder;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.properties.PropertiesContainer;

/**
 * All input for creating {@link DependencyResolver}s.
 */
@Builder
public record DependencyResolutionInput(
        ElementsContainer elementsContainer,
        PropertiesContainer propertiesContainer
) {
}
