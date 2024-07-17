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

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.Application;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.source.PreConstructedElementSource;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;

/**
 * A special {@link ElementContext} for {@link org.tframework.core.elements.annotations.PreConstructedElement}s.
 * In behaviour, these element contexts are similar to {@link SingletonElementContext}, the only difference is that
 * the instance is already created and does not have to be initialized.
 */
@Slf4j
public class PreConstructedElementContext extends ElementContext {

    private final Object preConstructedInstance;

    private PreConstructedElementContext(Object preConstructedInstance, String name) {
        super(
                name,
                preConstructedInstance.getClass(),
                ElementScope.SINGLETON,
                new PreConstructedElementSource(preConstructedInstance),
                DependencyResolutionInput.builder()
                        .application(Application.empty())
                        .build() //dependency resolution is not used by pre-constructed elements
        );
        this.preConstructedInstance = preConstructedInstance;
    }

    @Override
    public void initialize() {
        log.trace("Pre-constructed element '{}' does not need to be initialized, skipping...", name);
    }

    @Override
    protected InstanceRequest requestInstanceInternal(ElementDependencyGraph dependencyGraph) {
        return InstanceRequest.ofReused(preConstructedInstance);
    }

    @Override
    public String toString() {
        return "PreConstructedElementContext{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", scope=" + scope +
                '}';
    }

    /**
     * Creates a new {@link PreConstructedElementContext} for the given pre-constructed instance.
     * It will have a default name deduced from its type.
     */
    public static PreConstructedElementContext of(@NonNull Object preConstructedInstance) {
        return new PreConstructedElementContext(preConstructedInstance, Element.NAME_NOT_SPECIFIED);
    }

    /**
     * Creates a new {@link PreConstructedElementContext} for the given instance, with custom name.
     */
    public static PreConstructedElementContext of(@NonNull Object preConstructedInstance, @NonNull String name) {
        return new PreConstructedElementContext(preConstructedInstance, name);
    }

}
