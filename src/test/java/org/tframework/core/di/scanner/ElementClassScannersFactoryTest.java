/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.tframework.core.properties.PropertiesContainer;

class ElementClassScannersFactoryTest {

    static class RootClass {}

    @Test
    void shouldCreateRootElementClassScanner() {
        var scanner = ElementClassScannersFactory.createRootElementClassScanner(RootClass.class, PropertiesContainer.empty());
        assertNotNull(scanner);
    }
}
