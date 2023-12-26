/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
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
    void shouldCreateFixedClassScanner() {
        List<Class<?>> classesToScan = List.of(String.class, Integer.class);
        var scanner = ClassScannersFactory.createFixedClassScanner(classesToScan);

        assertEquals(classesToScan, scanner.getClasses());
    }

    @Test
    void shouldCreateNestedClassScanner() {
        var classToScan = this.getClass();
        var scanner = ClassScannersFactory.createNestedClassScanner(classToScan);

        assertEquals(classToScan, scanner.getClassToScan());
    }
}
