/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.elements.context.assembler.ClassElementContextAssembler;
import org.tframework.core.elements.context.assembler.ElementContextAssemblersFactory;
import org.tframework.core.elements.context.assembler.MethodElementContextAssembler;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementsInitializationProcessFactory {

    /**
     * Creates a {@link ElementsInitializationProcess}.
     */
    public static ElementsInitializationProcess createElementInitializationProcess() {
        ClassElementContextAssembler classContextAssembler = ElementContextAssemblersFactory.createDefaultClassElementContextAssembler();
        MethodElementContextAssembler methodContextAssembler = ElementContextAssemblersFactory.createDefaultMethodElementContextAssembler();
        return new ElementsInitializationProcess(classContextAssembler, methodContextAssembler);
    }

}
