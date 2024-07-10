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
package org.tframework.core.elements.context.assembler;

import java.lang.reflect.AnnotatedElement;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;
import org.tframework.core.elements.scanner.ElementScanner;
import org.tframework.core.elements.scanner.ElementScanningResult;

/**
 * The element context assembler creates {@link ElementContext} from an {@link ElementScanningResult}.
 * This result is typically produced by an {@link ElementScanner}.
 * @param <T> The type of component that had the {@link Element} annotation.
 */
public interface ElementContextAssembler<T extends AnnotatedElement> {

    /**
     * Assembles an {@link ElementContext}.
     * @param scanningResult The result of scanning for the {@link Element} annotation.
     * @param dependencyResolutionInput {@link DependencyResolutionInput} that will allow the created
     *                                  context to resolve its dependencies.
     * @throws ElementContextAssemblingException If the {@link ElementContext} could not be assembled.
     */
    ElementContext assemble(
            ElementScanningResult<T> scanningResult,
            DependencyResolutionInput dependencyResolutionInput
    ) throws ElementContextAssemblingException;

}
