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

import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;

/**
 * An {@link ElementContext} that represents a singleton element.
 * @see ElementScope#SINGLETON
 */
@Slf4j
public final class SingletonElementContext extends ElementContext {

    private Object instance;

    /**
     * Creates a singleton element context. For parameter details, see superclass constructor.
     */
    public SingletonElementContext(
            String name,
            Class<?> type,
            ElementSource source,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        super(name, type, ElementScope.SINGLETON, source, dependencyResolutionInput);
    }

    @Override
    public void initialize() {
        initialize(ElementDependencyGraph.empty());
    }

    private void initialize(ElementDependencyGraph dependencyGraph) {
        log.debug("Starting initialization of singleton element '{}'", name);
        if(instance == null) {
            instance = requestInstance(dependencyGraph);
            log.debug("Initialized singleton element context: {}. The instance was created: {}", name, instance);
        } else {
            log.debug("Singleton element '{}' was already eagerly initialized, skipping.", name);
        }
    }

    @Override
    protected InstanceRequest requestInstanceInternal(ElementDependencyGraph dependencyGraph) {
        if(instance == null) {
            instance = elementAssembler.assemble(dependencyGraph);
            return InstanceRequest.ofNewlyCreated(instance);
        } else {
            return InstanceRequest.ofReused(instance);
        }
    }

    @Override
    public String toString() {
        return "SingletonElementContext{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", source=" + source +
                '}';
    }
}
