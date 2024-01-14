/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SimpleConstructorScannerTest {

    private final SimpleConstructorScanner scanner = new SimpleConstructorScanner();

    @Test
    public void shouldGetOnlyDefaultConstructor_whenNoConstructorsCreated() {
        var constructors = scanner.getAllConstructors(OnlyDefaultConstructor.class);
        assertEquals(1, constructors.size());
    }


    @Test
    public void shouldGetAllConstructors_whenConstructorsAreAvailable() {
        var constructors = scanner.getAllConstructors(MultipleConstructors.class);
        assertEquals(2, constructors.size());
    }

    static class OnlyDefaultConstructor {}

    static class MultipleConstructors {
        public MultipleConstructors(int x) {}
        private MultipleConstructors(String s) {}
    }

}
