/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.handler;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.utils.TypeUtils;

/**
 * A {@link SpecialElementDependencyHandler} that handles dependencies with {@link Map} types, but
 * only when the keys of this map are {@link String}s. Assuming this is a {@code Map<String,V>} the handler
 * will attempt to load all elements of type {@code V} into the map, where the keys will be the element names.
 * For example, given 2 elements:
 * <ul>
 *     <li>{@code endpoint1} of type {@code Endpoint}.</li>
 *     <li>{@code endpoint2} of type {@code Endpoint}.</li>
 * </ul>
 * When using such dependency:
 * <pre>{@code
 * @InjectElement
 * private Map<String, Endpoint> endpoints;
 * }</pre>
 * This handler will fill the map with the following values:
 * <pre>{@code
 * {
 *     "endpoint1": [Endpoint 1 object],
 *     "endpoint2": [Endpoint 2 object]
 * }
 * }</pre>
 * If you want your element to ignore this handler, inject your element by name.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class StringMapDependencyHandler implements SpecialElementDependencyHandler {

    @Override
    public Optional<Object> handleSpecialDependency(
            ElementsContainer elementsContainer,
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph
    ) {
        if(Map.class.isAssignableFrom(dependencyDefinition.dependencyType())) {
            var keyType = TypeUtils.getTypeParameter(dependencyDefinition, 0);
            var valueType = TypeUtils.getTypeParameter(dependencyDefinition, 1);
            if(String.class.equals(keyType)) {
                log.debug("Found a map dependency with string keys and '{}' values.", valueType.getName());

                var elements = ElementUtils.getElementContexts(elementsContainer, valueType);
                log.debug("Found {} elements with type '{}', building map...", elements.size(), valueType.getName());

                var elementsMap = elements.stream()
                        .collect(Collectors.toMap(
                                ElementContext::getName,
                                context -> context.requestInstance(dependencyGraph)
                        ));
                return Optional.of(elementsMap);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
