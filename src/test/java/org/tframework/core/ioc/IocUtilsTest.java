/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.jupiter.api.Test;
import org.tframework.core.ioc.annotations.AfterConstruct;
import org.tframework.core.ioc.annotations.Injected;
import org.tframework.core.ioc.annotations.Managed;

class IocUtilsTest {

    @Test
    public void testMethodNameGeneratorNoParameters() {
        String name = IocUtils.createClassAndMethodName(MethodUtils.getAccessibleMethod(String.class, "length"));
        assertEquals("java.lang.String#length", name);
    }

    @Test
    public void testMethodNameGeneratorParameters() {
        String name = IocUtils.createClassAndMethodName(MethodUtils.getAccessibleMethod(String.class, "startsWith", String.class));
        assertEquals("java.lang.String#startsWith", name);
    }

    @Test
    public void testAnnotationListContainsAnnotation() {
        List<Class<? extends Annotation>> annotations = List.of(Injected.class, Managed.class);
        assertTrue(
                IocUtils.classListContainsAnnotation(annotations, () -> Managed.class)
        );
    }

    @Test
    public void testAnnotationListDoesNotContainAnnotation() {
        List<Class<? extends Annotation>> annotations = List.of(Injected.class, Managed.class);
        assertFalse(
                IocUtils.classListContainsAnnotation(annotations, () -> AfterConstruct.class)
        );
    }

    @Managed
    static class DefaultInjected {
        @Injected
        public String test;
        @Managed
        public void testMethod(String testParameter) {}
    }

    @Test
    public void testGetManagedClassNameDefault() {
        assertEquals(
                DefaultInjected.class.getName(),
                IocUtils.getReferencedEntityName(DefaultInjected.class)
        );
    }

    @Test
    public void testGetProvidedNameDefault() {
        Method method = MethodUtils.getMatchingMethod(DefaultInjected.class, "testMethod", String.class);
        assertEquals(
                "void",
                IocUtils.getProvidedEntityName(method)
        );
    }

    @Test
    public void testGetInjectedFieldNameDefault() throws Exception {
        DefaultInjected defaultInjected = new DefaultInjected();
        Field field = defaultInjected.getClass().getField("test");
        assertEquals(
                String.class.getName(),
                IocUtils.getReferencedEntityName(field)
        );
    }

    @Test
    public void testGetInjectedParameterNameDefault() {
        Parameter parameter = MethodUtils.getMatchingMethod(DefaultInjected.class, "testMethod", String.class).getParameters()[0];
        assertEquals(
                String.class.getName(),
                IocUtils.getReferencedEntityName(parameter)
        );
    }

    @Managed(name = "custom")
    static class CustomInjected {
        @Injected(name = "custom")
        public String test;
        @Managed(name = "custom")
        public void testMethod(@Injected(name = "custom") String testParameter) {}
    }

    @Test
    public void testGetManagedClassNameCustom() {
        assertEquals(
                "custom",
                IocUtils.getReferencedEntityName(CustomInjected.class)
        );
    }

    @Test
    public void testGetProvidedNameCustom() {
        Method method = MethodUtils.getMatchingMethod(CustomInjected.class, "testMethod", String.class);
        assertEquals(
                "custom",
                IocUtils.getProvidedEntityName(method)
        );
    }

    @Test
    public void testGetInjectedFieldNameCustom() throws Exception {
        CustomInjected customInjected = new CustomInjected();
        Field field = customInjected.getClass().getField("test");
        assertEquals(
                "custom",
                IocUtils.getReferencedEntityName(field)
        );
    }

    @Test
    public void testGetInjectedParameterNameCustom() {
        Parameter parameter = MethodUtils.getMatchingMethod(CustomInjected.class, "testMethod", String.class).getParameters()[0];
        assertEquals(
                "custom",
                IocUtils.getReferencedEntityName(parameter)
        );
    }
}