/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.handler;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.AmbiguousElementTypeException;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.utils.TypeUtils;

/**
 * This {@link SpecialElementDependencyHandler} is responsible for handling dependencies that are of type {@link Optional}.
 * <ul>
 *     <li>
 *         If there is exactly one element found by type which can fit into the {@link Optional}, then the
 *         {@link Optional} will be created with that element instance as value.
 *      </li>
 *      <li>
 *          If there is no element by type which can fit into the {@link Optional}, an empty {@link Optional}
 *          will be used.
 *      </li>
 *      <li>
 *          Any exception such as {@link AmbiguousElementTypeException} will not be handled.
 *      </li>
 * </ul>
 * If you want your element to ignore this handler, inject your element by name.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class OptionalDependencyHandler implements SpecialElementDependencyHandler {

    @Override
    public Optional<Object> handleSpecialDependency(
            ElementsContainer elementsContainer,
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph
    ) {
        if(Optional.class.isAssignableFrom(dependencyDefinition.dependencyType())) {
            var optionalItemType = TypeUtils.getTypeParameter(dependencyDefinition);
            log.debug("Optional dependency detected, with item type '{}'", optionalItemType.getName());

            var elementInstance = elementsContainer.getElementContext(optionalItemType)
                    .requestInstance(dependencyGraph);
            //the double Optional is intentional here, as the 'SpecialElementDependencyHandler' also uses it
            return Optional.of(Optional.of(elementInstance));
        } else {
            //this dependency is not an optional, ignoring it
            return Optional.empty();
        }
    }
}
