/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

class MethodScannersFactoryTest {

    @Test
    void shouldCreateDefaultMethodScanner() {
        var scanner = MethodScannersFactory.createDefaultMethodScanner();
        assertInstanceOf(DeclaredMethodScanner.class, scanner);
    }
}
