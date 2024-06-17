/* Licensed under Apache-2.0 2024. */
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
