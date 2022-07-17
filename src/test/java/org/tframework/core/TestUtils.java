/* Licensed under Apache-2.0 2022. */
package org.tframework.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.lang.reflect.Method;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.jupiter.api.Test;

/**
 * Test utility methods.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtils {

    /**
     * Invokes a private method of an object. Useful for testing private methods.
     * @param targetObject The object whose method will be invoked.
     * @param methodName The name of the method.
     * @param parameters The parameters to be passed to the method.
     * @param <T> Return type of the method.
     * @return The return value of the invoked method.
     * @throws RuntimeException If there is no such method, or there is parameter/return type mismatch.
     */
    public static <T> T invokePrivateMethod(Object targetObject, String methodName, Object... parameters) throws RuntimeException {
        try {
            Class<?>[] parameterTypes = new Class<?>[parameters.length];
            for(int i = 0; i < parameters.length; i++) {
                parameterTypes[i] = parameters[i].getClass();
            }
            Method method = MethodUtils.getMatchingMethod(targetObject.getClass(), methodName, parameterTypes);
            method.setAccessible(true);
            return (T)method.invoke(targetObject, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class TestClass {
        public static final String SOMETHING = "something";
        private String getSomething() {
            return SOMETHING;
        }
        private String getSomethingWithParams(File f, String s) {
            return SOMETHING;
        }
        private int getTwo() {
            return 2;
        }
    }

    @Test
    public void testInvokePrivateMethodNoParam() {
        TestClass testClass = new TestClass();
        String result = invokePrivateMethod(testClass, "getSomething");
        assertEquals(TestClass.SOMETHING, result);
    }

    @Test
    public void testInvokePrivateMethodMultipleParams() {
        TestClass testClass = new TestClass();
        String result = invokePrivateMethod(testClass, "getSomethingWithParams", new File(""), "");
        assertEquals(TestClass.SOMETHING, result);
    }

    @Test
    public void testInvokePrivateMethodPrimitiveReturnType() {
        TestClass testClass = new TestClass();
        int result = invokePrivateMethod(testClass, "getTwo");
        assertEquals(2, result);
    }
}
