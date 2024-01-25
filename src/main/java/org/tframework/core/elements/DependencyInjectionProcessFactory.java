package org.tframework.core.elements;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.elements.context.assembler.ClassElementContextAssembler;
import org.tframework.core.elements.context.assembler.ElementContextAssemblersFactory;
import org.tframework.core.elements.context.assembler.MethodElementContextAssembler;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DependencyInjectionProcessFactory {

    /**
     * Creates a {@link DependencyInjectionProcess} that performs dependency injection.
     */
    public static DependencyInjectionProcess createDependencyInjectionProcess() {
        ClassElementContextAssembler classContextAssembler = ElementContextAssemblersFactory.createDefaultClassElementContextAssembler();
        MethodElementContextAssembler methodContextAssembler = ElementContextAssemblersFactory.createDefaultMethodElementContextAssembler();
        return new DependencyInjectionProcess(classContextAssembler, methodContextAssembler);
    }

}
