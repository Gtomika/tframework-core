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
