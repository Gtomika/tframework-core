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
package org.tframework.core.elements.scanner;

import java.util.List;
import lombok.Builder;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.assembler.ClassElementContextAssembler;
import org.tframework.core.elements.context.assembler.ElementContextAssembler;
import org.tframework.core.elements.context.assembler.MethodElementContextAssembler;

/**
 * A collection of all kind of scanners, assemblers and filters that will be used
 * by the framework to detect, create and process {@link ElementContext}s.
 * @param elementClassScanners Scanners that will find element classes.
 * @param elementMethodScanners Scanners that will find element methods.
 * @param classElementContextAssembler An {@link ElementContextAssembler} for creating {@link ElementContext}s from classes.
 * @param methodElementContextAssembler An {@link ElementContextAssembler} for creating {@link ElementContext}s from methods.
 */
@Builder
public record ElementContextBundle(
        List<ElementClassScanner> elementClassScanners,
        List<ElementMethodScanner> elementMethodScanners,
        ClassElementContextAssembler classElementContextAssembler,
        MethodElementContextAssembler methodElementContextAssembler
) {
}
