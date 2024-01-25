package org.tframework.core.elements.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class PreConstructedElementContextTest {

    @Test
    public void shouldUseProvidedInstanceForPreConstructedElement() {
        var instance = new Object();
        var elementContext = PreConstructedElementContext.of(instance);
        elementContext.initialize(null);

        var requestedInstance = elementContext.requestInstance();

        assertSame(instance, requestedInstance);
    }

}