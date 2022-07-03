/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    }

}