/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

class NestedClassScannerTest {

    @Test
    public void shouldFindNestedClasses() {
        var scanner = new NestedClassScanner(NestedClassScannerTest.class);
        Set<Class<?>> scannedClasses = scanner.scanClasses();

        assertEquals(3, scannedClasses.size());
        assertTrue(scannedClasses.stream().anyMatch(c -> c.equals(NestedClassScannerTest.class)));
        assertTrue(scannedClasses.stream().anyMatch(c -> c.equals(NestedClass1.class)));
        assertTrue(scannedClasses.stream().anyMatch(c -> c.equals(NestedClass2.class)));
    }

    static class NestedClass1 {}

    static class NestedClass2 {}

}
