/* Licensed under Apache-2.0 2024. */
package org.tframework.elements.dependency;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class DependencyByNameTest {

    @Element(name = "string1")
    public String provideString1() {
        return "string1";
    }

    @Element(name = "string2")
    public String provideString2() {
        return "string2";
    }

    @Test
    public void shouldInjectElementByName(@InjectElement("string2") String string2) {
        assertEquals("string2", string2);
    }
}
