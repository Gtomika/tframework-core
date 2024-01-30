/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class PreConstructedElementContextTest {

    @Test
    public void shouldUseProvidedInstanceForPreConstructedElement() {
        var instance = new Object();
        var elementContext = PreConstructedElementContext.of(instance);
        elementContext.initialize();

        var requestedInstance = elementContext.requestInstance();

        assertSame(instance, requestedInstance);
    }

}
