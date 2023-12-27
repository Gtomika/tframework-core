/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CliUtilsTest {

    @Test
    public void shouldDetectArgumentWithKey_ifCorrectKey() {
        String arg = "k1" + CliUtils.CLI_KEY_VALUE_SEPARATOR + "v1";
        assertTrue(CliUtils.isArgumentWithKey(arg, "k1"));
    }

    @Test
    public void shouldNotDetectArgumentWithKey_ifIncorrectKey() {
        String arg = "k1" + CliUtils.CLI_KEY_VALUE_SEPARATOR + "v1";
        assertFalse(CliUtils.isArgumentWithKey(arg, "k2"));
    }

    @Test
    public void shouldNotDetectArgumentWithKey_ifNoKey() {
        String arg = "randomArg";
        assertFalse(CliUtils.isArgumentWithKey(arg, "k1"));
    }

    @Test
    public void shouldExtractValue_ifStandardKeyValueFormat() {
        String arg = "k1" + CliUtils.CLI_KEY_VALUE_SEPARATOR + "v1";
        String argValue = CliUtils.extractArgumentValue(arg);
        assertEquals("v1", argValue);
    }

    @Test
    public void shouldExtractValue_ifSeparatorPresentMultipleTimes() {
        String complexValue = "v1" + CliUtils.CLI_KEY_VALUE_SEPARATOR + "v2";
        String arg = "k1" + CliUtils.CLI_KEY_VALUE_SEPARATOR + complexValue;
        String argValue = CliUtils.extractArgumentValue(arg);
        assertEquals(complexValue, argValue);
    }

    @Test
    public void shouldExtractValue_ifEmpty() {
        String arg = "k1" + CliUtils.CLI_KEY_VALUE_SEPARATOR;
        String argValue = CliUtils.extractArgumentValue(arg);
        assertEquals("", argValue);
    }

}
