/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.List;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.tframework.core.ioc.annotations.Managed;

class ManagedEntityConstructorTest {

    static class TestClassNoArgsConstructor {
        public TestClassNoArgsConstructor() {}
    }

    @Test
    public void testConstructionMethodIsNoArgsConstructor() {
        var constructor = new ManagedEntityConstructor<>(TestClassNoArgsConstructor.class);
        var test = constructor.constructManagedEntity(List.of());
        assertNotNull(test);
    }

    static class TestClassOneArgConstructor {
        public TestClassOneArgConstructor(int i) {}
    }

    @Test
    public void testConstructibleWithPublicConstructor() {
        assertDoesNotThrow(() -> new ManagedEntityConstructor<>(TestClassOneArgConstructor.class));
    }

    @Managed
    static class ManagedClass {
        @Managed
        public TestClassNoArgsConstructor something() {return new TestClassNoArgsConstructor(); };
    }

    @Test
    @Disabled("Provider needs declaring instance, and that needs application context. TODO")
    public void testConstructWithProvider() {
        Method provider = MethodUtils.getMatchingMethod(ManagedClass.class, "something");
        var constructor = new ManagedEntityConstructor<>(TestClassNoArgsConstructor.class, provider);
        TestClassNoArgsConstructor t = constructor.constructManagedEntity(List.of());
        assertNotNull(t);
    }

}