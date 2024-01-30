/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class DeclaredMethodScannerTest {

    private final  DeclaredMethodScanner scanner = new DeclaredMethodScanner();

    @Test
    public void shouldScanMethods() {
        var scannedMethods = scanner.scanMethods(Child.class)
                .stream()
                .filter(m -> !m.getName().startsWith("$")) //filter out synthetic methods added by tools like JaCoCo
                .collect(Collectors.toSet());

        assertEquals(3, scannedMethods.size());
        assertTrue(scannedMethods.stream().anyMatch(m -> m.getName().equals("publicChildMethod")));
        assertTrue(scannedMethods.stream().anyMatch(m -> m.getName().equals("protectedChildMethod")));
        assertTrue(scannedMethods.stream().anyMatch(m -> m.getName().equals("privateChildMethod")));
    }

    static class Parent {
        public void parentMethod() {}
    }

    static class Child extends Parent {
        public void publicChildMethod() {}
        protected void protectedChildMethod() {}
        private void privateChildMethod() {}
    }

}
