/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.field;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SimpleFieldScannerTest {

    private final SimpleFieldScanner scanner = new SimpleFieldScanner();

    @Test
    public void shouldGetAllFields() {
        var fields = scanner.getAllFields(TestClass.class);
        assertTrue(fields.stream().anyMatch(f -> f.getName().equals("field1")));
        assertTrue(fields.stream().anyMatch(f -> f.getName().equals("field2")));
        assertTrue(fields.stream().anyMatch(f -> f.getName().equals("field3")));
    }

    static class TestClass {

        public int field1;

        private String field2;

        public static final boolean field3 = true;

    }


}
