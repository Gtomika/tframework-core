/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context.assembler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ElementContextAssemblersFactoryTest {

    @Test
    void shouldCreateDefaultClassElementContextAssembler() {
        var assembler = ElementContextAssemblersFactory.createDefaultClassElementContextAssembler();
        assertInstanceOf(ClassElementContextAssembler.class, assembler);
    }

    @Test
    void shouldCreateDefaultMethodElementContextAssembler() {
        var assembler = ElementContextAssemblersFactory.createDefaultMethodElementContextAssembler();
        assertInstanceOf(MethodElementContextAssembler.class, assembler);
    }
}
