/* Licensed under Apache-2.0 2024. */
package org.tframework.elements;

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
