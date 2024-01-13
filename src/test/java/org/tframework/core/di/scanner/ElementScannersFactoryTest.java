/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.tframework.core.properties.PropertiesContainer;

class ElementScannersFactoryTest {

    static class RootClass {}

    @Test
    void shouldCreateRootElementClassScanner() {
        var scanner = ElementScannersFactory.createRootElementClassScanner(RootClass.class, PropertiesContainer.empty());
        assertNotNull(scanner);
    }

    @Test
    void shouldCreateInternalElementClassScanner() {
        var scanner = ElementScannersFactory.createInternalElementClassScanner(PropertiesContainer.empty());
        assertNotNull(scanner);
    }

    @Test
    void shouldCreatePackagesElementClassScanner() {
        var scanner = ElementScannersFactory.createPackagesElementClassScanner(PropertiesContainer.empty());
        assertNotNull(scanner);
    }

    @Test
    void shouldCreateClassesElementClassScanner() {
        var scanner = ElementScannersFactory.createClassesElementClassScanner(PropertiesContainer.empty());
        assertNotNull(scanner);
    }

    @Test
    void shouldCreateFixedClassesElementMethodScanner() {
        var scanner = ElementScannersFactory.createFixedClassesElementMethodScanner(Set.of());
        assertNotNull(scanner);
    }
}
