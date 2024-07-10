/*
Copyright 2023 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
