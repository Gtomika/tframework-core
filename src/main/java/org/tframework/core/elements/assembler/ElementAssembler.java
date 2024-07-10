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
package org.tframework.core.elements.assembler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;

/**
 * The element assembler is responsible for constructing an instance of an element.
 * Subclasses may use different {@link org.tframework.core.elements.context.source.ElementSource}s and strategies to
 * construct the element.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ElementAssembler {

    protected final String elementName;
    protected final Class<?> elementType;

    /**
     * Constructs an instance of the element.
     * @param dependencyGraph {@link ElementDependencyGraph} that stores the current state of the dependency resolution.
     *                        This is to avoid invalid dependency relations, like circular dependencies.
     * @return The constructed element.
     * @throws ElementAssemblingException If the element could not be constructed. This exception contains the cause
     * of the failure, and other useful details.
     */
    public abstract Object assemble(ElementDependencyGraph dependencyGraph) throws ElementAssemblingException;

}
