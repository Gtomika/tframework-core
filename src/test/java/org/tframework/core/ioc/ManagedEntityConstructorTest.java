/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.tframework.core.ioc.exceptions.NotConstructibleException;

class ManagedEntityConstructorTest {

    static class TestClassNoArgsConstructor {
        public TestClassNoArgsConstructor() {}
    }

    @Test
    public void testConstructionMethodIsNoArgsConstructor() {
        var test = ManagedEntityConstructor.constructManagedEntity(TestClassNoArgsConstructor.class);
        assertNotNull(test);
    }

    static class TestClassOneArgConstructor {
        public TestClassOneArgConstructor(int i) {}
    }

    @Test
    public void testNotConstructible() {
        assertThrows(NotConstructibleException.class, () ->
                ManagedEntityConstructor.constructManagedEntity(TestClassOneArgConstructor.class));
    }

}