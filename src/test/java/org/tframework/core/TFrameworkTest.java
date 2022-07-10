/* Licensed under Apache-2.0 2022. */
package org.tframework.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TFrameworkTest {

    @TFrameworkRoot
    static class TestClass {}

    @Test
    public void testFindsClassAnnotatedWithRoot() {
        Class<?> rootClass = TFramework.findClassAnnotatedWithTFrameworkRoot();
        assertEquals(TestClass.class, rootClass);
    }

    //TODO: how to test no root class case, multiple root class case?!
    //TODO: maybe separate test classes, with separate class loader?

}