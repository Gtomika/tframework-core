/*
Copyright 2023 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
