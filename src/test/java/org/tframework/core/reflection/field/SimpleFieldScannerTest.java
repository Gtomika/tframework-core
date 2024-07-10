/*
Copyright 2024 Tamas Gaspar

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
