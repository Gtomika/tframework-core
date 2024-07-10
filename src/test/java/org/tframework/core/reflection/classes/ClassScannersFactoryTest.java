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
    void shouldCreateNestedClassScanner() {
        var classToScan = this.getClass();
        var scanner = ClassScannersFactory.createNestedClassScanner(classToScan);

        assertEquals(classToScan, scanner.getClassToScan());
    }
}
