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
package org.tframework.test.elements.scanning;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class ElementClassScanningTest {

    @Element(name = "some_element")
    public static class SomeElement {}

    @Element(name = "other_element")
    public static class OtherElement {

        private final SomeElement someElement;

        public OtherElement(SomeElement someElement) {
            this.someElement = someElement;
        }
    }

    @Test
    public void shouldScanElementClasses(
            @InjectElement("some_element") SomeElement someElement,
            @InjectElement("other_element") OtherElement otherElement
    ) {
        assertNotNull(otherElement.someElement);
    }
}
