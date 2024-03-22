/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.source;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ClassElementSourceTest {

    static class TestClass {
        public TestClass(Integer x, String y) {}
    }

    @Test
    void shouldReturnElementConstructionParameters() {
        var testConstructor = TestClass.class.getConstructors()[0];
        var source = new ClassElementSource(TestClass.class, testConstructor);

        var parameters = source.elementConstructionParameters();
        assertEquals(2, parameters.size());
        assertEquals(Integer.class, parameters.get(0).getType());
        assertEquals(String.class, parameters.get(1).getType());
    }
}
