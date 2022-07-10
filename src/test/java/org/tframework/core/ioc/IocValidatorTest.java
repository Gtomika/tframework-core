/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Scanner;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.tframework.core.ioc.annotations.Managed;

class IocValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "org.tframework.ApplicationContext",
            "Test5",
            "Test.This.Name",
            "Test-This.Name",
            "123-Test.This.name"
    })
    public void testValidName(String name) {
        assertDoesNotThrow(() -> IocValidator.validateEntityName(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Test$",
        "Test/This/Name"
    })
    public void testInvalidName(String name) {
        assertThrows(IllegalArgumentException.class, () -> IocValidator.validateEntityName(name));
    }

    @Managed
    static class ValidProviderMethod {
        @Managed
        public Object getSomething() { return new Object(); }
    }

    @Test
    public void testValidProviderMethod() {
        Method method = MethodUtils.getMatchingMethod(ValidProviderMethod.class, "getSomething");
        assertDoesNotThrow(() -> IocValidator.validateProviderMethod(method, null));
    }

    static class ProviderNotInManagedClass {
        @Managed
        public Object getSomething() { return new Object(); }
    }

    @Test
    public void testInvalidProviderMethodNotInManagedClass() {
        Method method = MethodUtils.getMatchingMethod(ProviderNotInManagedClass.class, "getSomething");
        var e = assertThrows(IllegalArgumentException.class, () -> IocValidator.validateProviderMethod(method, null));
        assertTrue(e.getMessage().contains(IocUtils.createClassAndMethodName(method)));
    }

    @Managed
    static class ProviderNoReturnType {
        @Managed
        public void getSomething() {}
    }

    @Test
    public void testInvalidProviderMethodNoReturnType() {
        Method method = MethodUtils.getMatchingMethod(ProviderNoReturnType.class, "getSomething");
        var e = assertThrows(IllegalArgumentException.class, () -> IocValidator.validateProviderMethod(method, null));
        assertTrue(e.getMessage().contains(IocUtils.createClassAndMethodName(method)));
    }

    @Managed
    static class ProviderNotPublic {
        @Managed
        private Object getSomething() { return new Object(); }
    }

    @Test
    public void testInvalidProviderMethodNotPublic() {
        Method method = MethodUtils.getMatchingMethod(ProviderNotPublic.class, "getSomething");
        var e = assertThrows(IllegalArgumentException.class, () -> IocValidator.validateProviderMethod(method, null));
        assertTrue(e.getMessage().contains(IocUtils.createClassAndMethodName(method)));
    }

    @Test
    public void testProviderMatchingReturnType() {
        Method method = MethodUtils.getMatchingMethod(ValidProviderMethod.class, "getSomething");
        assertDoesNotThrow(() -> IocValidator.validateProviderMethod(method, Object.class));
    }

    @Test
    public void testProviderNotMatchingReturnType() {
        Method method = MethodUtils.getMatchingMethod(ValidProviderMethod.class, "getSomething");
        assertThrows(IllegalArgumentException.class, () -> IocValidator.validateProviderMethod(method, Scanner.class));
    }

    @Managed
    static class ProviderPrimitiveReturnType {
        @Managed
        public int getSomething() { return 5; }
    }

    @Test
    public void testProviderMatchingReturnTypePrimitive() {
        Method method = MethodUtils.getMatchingMethod(ProviderPrimitiveReturnType.class, "getSomething");
        assertDoesNotThrow(() -> IocValidator.validateProviderMethod(method, Integer.class));
    }

    @Managed
    static class ProviderStatic {
        @Managed
        public static String getString() {return ""; }
    }

    @Test
    public void testProviderStatic() {
        Method method = MethodUtils.getMatchingMethod(ProviderStatic.class, "getString");
        assertThrows(IllegalArgumentException.class, () -> IocValidator.validateProviderMethod(method, null));
    }

    @Managed
    static class ProviderWithParams {
        @Managed
        public String getString(int x) {return x + "x"; }
    }

    @Test
    public void testProviderWithParams() {
        Method method = MethodUtils.getMatchingMethod(ProviderWithParams.class, "getString", Integer.class);
        assertThrows(IllegalArgumentException.class, () -> IocValidator.validateProviderMethod(method, null));
    }

}