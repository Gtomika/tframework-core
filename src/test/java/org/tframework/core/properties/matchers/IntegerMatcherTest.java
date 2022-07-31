/* Licensed under Apache-2.0 2022. */
package org.tframework.core.properties.matchers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class IntegerMatcherTest {

    private IntegerMatcher integerMatcher;

    @BeforeEach
    public void setUp() {
        integerMatcher = new IntegerMatcher();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "-123", "0", "-2", "15975"})
    public void testMatchesIntegers(String rawProperty) {
        assertTrue(integerMatcher.matchesRawProperty(rawProperty));
    }

    @ParameterizedTest
    @ValueSource(strings = {"01", "-0123", "5.43", "Hello", "true"})
    public void testNotMatchesInvalidIntegers(String rawProperty) {
        assertFalse(integerMatcher.matchesRawProperty(rawProperty));
    }

}