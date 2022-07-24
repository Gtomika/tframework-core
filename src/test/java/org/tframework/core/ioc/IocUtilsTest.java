/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
                IocUtils.getReferencedEntityName(field)
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
                IocUtils.getReferencedEntityName(field)
        );
    }
}