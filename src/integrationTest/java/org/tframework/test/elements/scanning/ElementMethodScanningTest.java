/* Licensed under Apache-2.0 2024. */
package org.tframework.test.elements.scanning;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class ElementMethodScanningTest {

    //note: not marked as element here on the class
    public static class SomeElement {

        private final OtherElement otherElement;

        public SomeElement(OtherElement otherElement) {
            this.otherElement = otherElement;
        }
    }

    @Element(name = "other_element")
    public static class OtherElement {}

    @Element(name = "some_element")
    public SomeElement provideSomeElement(OtherElement otherElement) {
        return new SomeElement(otherElement);
    }

    @Test
    public void shouldScanElementMethod(@InjectElement SomeElement someElement) {
        assertNotNull(someElement.otherElement);
    }

}
