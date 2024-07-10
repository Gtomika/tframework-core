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
import org.tframework.core.elements.dependency.DependencyDefinition;

/**
 * Resolves basic (non-element) dependency definitions into dependency values. These dependency resolvers do
 * not start additional resolutions recursively, instead they simply return the dependency value.
 */
public non-sealed interface BasicDependencyResolver extends DependencyResolver {

    /**
     * Resolves the dependency definition into a dependency value.
     * @param dependencyDefinition The {@link DependencyDefinition} that should be resolved.
     * @return The resolved dependency value, or empty if this resolver could not resolve this dependency.
     */
    Optional<Object> resolveDependency(DependencyDefinition dependencyDefinition);

}
