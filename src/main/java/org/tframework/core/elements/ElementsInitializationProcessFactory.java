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
package org.tframework.core.elements;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.tframework.core.elements.context.assembler.ElementContextAssemblersFactory;
import org.tframework.core.elements.scanner.ElementContextBundle;
import org.tframework.core.elements.scanner.ElementScannersFactory;

/**
 * Creates {@link ElementsInitializationProcess} and related objects.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementsInitializationProcessFactory {

    /**
     * Creates a {@link ElementsInitializationProcess}.
     */
    public static ElementsInitializationProcess createElementInitializationProcess() {
        return new ElementsInitializationProcess();
    }

    /**
     * Creates a default {@link ElementContextBundle} that is required by the {@link ElementsInitializationProcess}.
     */
    public static ElementContextBundle createDefaultElementContextBundle(@NonNull ElementsInitializationInput input) {
        var classElementScanners = ElementScannersFactory.createDefaultElementClassScanners(input);
        var methodElementScanners = ElementScannersFactory.createDefaultElementMethodScanners(input);
        var classContextAssembler = ElementContextAssemblersFactory.createDefaultClassElementContextAssembler();
        var methodContextAssembler = ElementContextAssemblersFactory.createDefaultMethodElementContextAssembler();

        return ElementContextBundle.builder()
                .elementClassScanners(classElementScanners)
                .elementMethodScanners(methodElementScanners)
                .classElementContextAssembler(classContextAssembler)
                .methodElementContextAssembler(methodContextAssembler)
                .build();
    }

}
