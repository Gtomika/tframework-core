/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.constructor;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

class ConstructorScannersFactoryTest {

    @Test
    void shouldCreateDefaultConstructorScanner() {
        ConstructorScanner constructorScanner = ConstructorScannersFactory.createDefaultConstructorScanner();
        assertInstanceOf(SimpleConstructorScanner.class, constructorScanner);
    }
}
