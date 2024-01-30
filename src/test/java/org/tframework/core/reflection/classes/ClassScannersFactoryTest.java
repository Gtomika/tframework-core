/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import org.junit.jupiter.api.Test;

class ClassScannersFactoryTest {

    @Test
    void shouldCreatePackageClassScanner() {
        var packages = Set.of("a.b.c");
        var scanner = ClassScannersFactory.createPackageClassScanner(packages);

        assertEquals(packages, scanner.getPackageNames());
    }

    @Test
    void shouldCreateNestedClassScanner() {
        var classToScan = this.getClass();
        var scanner = ClassScannersFactory.createNestedClassScanner(classToScan);

        assertEquals(classToScan, scanner.getClassToScan());
    }
}
