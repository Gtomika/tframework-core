/* Licensed under Apache-2.0 2022. */
package org.tframework.core.properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.tframework.core.properties.annotations.PropertyMatcherImplementation;
import org.tframework.core.properties.exceptions.PropertyException;
import org.tframework.core.properties.exceptions.PropertyMatcherException;
import org.tframework.core.properties.matchers.IntegerMatcher;
import org.tframework.core.properties.matchers.PropertyMatcher;

class PropertyValidatorTest {

    @Test
    public void testValidatePropertyMatcherValid() {
        assertDoesNotThrow(() -> PropertyValidator.validatePropertyMatcher(IntegerMatcher.class));
    }

    @PropertyMatcherImplementation
    static class InvalidPropertyMatcher1 {}

    @Test
    public void testValidatePropertyMatcherNoInterface() {
        assertThrows(PropertyMatcherException.class, () -> PropertyValidator.validatePropertyMatcher(InvalidPropertyMatcher1.class));
    }

    @PropertyMatcherImplementation
    @RequiredArgsConstructor
    static class InvalidPropertyMatcher2 implements PropertyMatcher<String> {
        private final int dummy;
        @Override
        public boolean matchesRawProperty(String rawProperty) {return false;}
        @Override
        public String fromRawProperty(String rawProperty) {return null;}
        @Override
        public Class<String> getPropertyType() {return String.class;}
    }

    @Test
    public void testValidatePropertyMatcherNoConstructor() {
        assertThrows(PropertyMatcherException.class, () -> PropertyValidator.validatePropertyMatcher(InvalidPropertyMatcher2.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"my.custom.property","property.with_underscore","a.b.c"})
    public void testValidatePropertyName(String name) {
        assertDoesNotThrow(() -> PropertyValidator.validatePropertyName(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"can.not.have.number4", "empty..section", ""})
    @NullSource
    public void testValidatePropertyNameInvalid(String name) {
        assertThrows(PropertyException.class, () -> PropertyValidator.validatePropertyName(name));
    }

}