/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.jupiter.api.Test;
import org.tframework.core.ioc.annotations.Managed;
import org.tframework.core.ioc.exceptions.NotConstructibleException;

class ManagedEntityConstructorTest {

    static class TestClassNoArgsConstructor {
        public TestClassNoArgsConstructor() {}
    }

    @Test
    public void testConstructionMethodIsNoArgsConstructor() {
        var constructor = new ManagedEntityConstructor<>(TestClassNoArgsConstructor.class);
        var test = constructor.constructManagedEntity();
        assertNotNull(test);
    }

    static class TestClassOneArgConstructor {
        public TestClassOneArgConstructor(int i) {}
    }

    @Test
    public void testNotConstructible() {
        var e = assertThrows(NotConstructibleException.class,
                () -> new ManagedEntityConstructor<>(TestClassOneArgConstructor.class));
        System.out.println(e.getMessage());
    }

    @Managed
    static class ManagedClass {
        @Managed
        public TestClassNoArgsConstructor something() {return new TestClassNoArgsConstructor(); };
    }

    @Test
    public void testConstructWithProvider() {
        Method provider = MethodUtils.getMatchingMethod(ManagedClass.class, "something");
        var constructor = new ManagedEntityConstructor<>(TestClassNoArgsConstructor.class, provider);
        TestClassNoArgsConstructor t = constructor.constructManagedEntity();
        assertNotNull(t);
    }

}