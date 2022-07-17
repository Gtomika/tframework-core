/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tframework.core.TestUtils;
import org.tframework.core.ioc.annotations.Injected;

class DependencyResolverTest {

    private DependencyResolver dependencyResolver;

    @BeforeEach
    public void setUp() {
        //TODO: set application context instance up with mock managed entities
        dependencyResolver = new DependencyResolver();
    }

    static class DefaultInjected {
        @Injected
        public String test;
    }

    @Test
    public void testGetInjectedEntityNameDefault() throws Exception {
        DefaultInjected defaultInjected = new DefaultInjected();
        Field field = defaultInjected.getClass().getField("test");
        assertEquals(
                String.class.getName(),
                TestUtils.invokePrivateMethod(dependencyResolver, "getDependencyEntityName",
                        field, field.getAnnotation(Injected.class))
        );
    }

    static class CustomInjected {
        @Injected(name = "custom")
        public String test;
    }

    @Test
    public void testGetInjectedEntityNameCustom() throws Exception {
        CustomInjected customInjected = new CustomInjected();
        Field field = customInjected.getClass().getField("test");
        assertEquals(
                "custom",
                TestUtils.invokePrivateMethod(dependencyResolver, "getDependencyEntityName",
                        field, field.getAnnotation(Injected.class))
        );
    }

    @Test
    public void testIsSelfDependencyTrue() {
        assertTrue(TestUtils.invokePrivateMethod(dependencyResolver, "isSelfDependency", "dp", "dp"));
    }

    @Test
    public void testIsSelfDependencyFalse() {
        assertFalse(TestUtils.invokePrivateMethod(dependencyResolver, "isSelfDependency", "dp", "not dp"));
    }
}