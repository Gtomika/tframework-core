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
