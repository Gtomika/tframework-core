/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.ElementUtils;

class PreConstructedElementContextTest {

    @Test
    public void shouldUseProvidedInstanceForPreConstructedElement() {
        var instance = new Object();
        var elementContext = PreConstructedElementContext.of(instance);
        elementContext.initialize();

        var requestedInstance = elementContext.requestInstance();

        assertSame(instance, requestedInstance);
        assertEquals(ElementUtils.getElementNameByType(instance.getClass()), elementContext.name);
    }

}
