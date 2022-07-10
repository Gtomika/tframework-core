/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.jupiter.api.Test;

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

}