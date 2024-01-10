/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class NestedClassScannerTest {

    @Test
    public void shouldFindNestedClasses() {
        var scanner = new NestedClassScanner(NestedClassScannerTest.class);
        var scannedClasses = scanner.scanClasses();

        assertEquals(3, scannedClasses.size());
        assertTrue(scannedClasses.stream().anyMatch(c -> c.equals(NestedClassScannerTest.class)));
        assertTrue(scannedClasses.stream().anyMatch(c -> c.equals(NestedClass1.class)));
        assertTrue(scannedClasses.stream().anyMatch(c -> c.equals(NestedClass2.class)));
    }

    @Test
    public void shouldFindNestedClasses_butNotOuterClassContainingScannedClass() {
        var scanner = new NestedClassScanner(NestedClass1.class);
        var scannedClasses = scanner.scanClasses();

        assertEquals(1, scannedClasses.size()); //must not include the parent of the scanned class
        assertTrue(scannedClasses.stream().anyMatch(c -> c.equals(NestedClass1.class)));
    }

    static class NestedClass1 {}

    static class NestedClass2 {}

}
