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
package org.tframework.core.elements.dependency.resolver;

import java.util.Optional;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.reflection.annotations.PreScannedAnnotations;

/**
 * Resolves element dependency definitions into element instances. This requires more complex logic
 * than the {@link BasicDependencyResolver}s, because each dependency resolution can trigger additional resolutions,
 * and we have to avoid getting into infinite loops (such as circular dependencies).
 */
public non-sealed interface ElementDependencyResolver extends DependencyResolver {

    /**
     * Resolves the given definition into an element instance.
     * @param dependencyDefinition {@link DependencyDefinition} which describes the element dependency.
     * @param originalElementContext The original {@link ElementContext} whose dependencies are being resolved.
     * @param dependencyGraph {@link ElementDependencyGraph} with the current state of the dependency resolution.
     * @param preScannedAnnotations {@link PreScannedAnnotations} with the annotations data of the dependency.
     * @return {@link Optional} with the element instance, if resolved, empty if not.
     */
    Optional<Object> resolveDependency(
            DependencyDefinition dependencyDefinition,
            ElementContext originalElementContext,
            ElementDependencyGraph dependencyGraph,
            PreScannedAnnotations preScannedAnnotations
    );

}
