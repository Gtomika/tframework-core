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
package org.tframework.core.elements.context;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;

/**
 * An {@link ElementContext} that represents a prototype element.
 * @see ElementScope#PROTOTYPE
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public final class PrototypeElementContext extends ElementContext {

    private final List<Object> instances;

    /**
     * Creates a prototype element context. For the parameter documentation, see the superclass constructor.
     */
    public PrototypeElementContext(
            String name,
            Class<?> type,
            ElementSource source,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        super(name, type, ElementScope.PROTOTYPE, source, dependencyResolutionInput);
        this.instances = new ArrayList<>();
    }

    @Override
    public void initialize() {
        log.debug("Prototype element context '{}' does not need any initialization, skipping.", name);
    }

    @Override
    protected InstanceRequest requestInstanceInternal(ElementDependencyGraph dependencyGraph) {
        Object instance = elementAssembler.assemble(dependencyGraph);

        instances.add(instance);
        log.debug("Created new instance of prototype element: {}", name);
        return InstanceRequest.ofNewlyCreated(instance);
    }

    @Override
    public String toString() {
        return "PrototypeElementContext{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", source=" + source +
                '}';
    }
}
