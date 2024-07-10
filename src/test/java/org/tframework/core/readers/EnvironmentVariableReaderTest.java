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
package org.tframework.core.readers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EnvironmentVariableReaderTest {

    private static final String TEST_VARIABLE_NAME = "TEST_VAR";
    private static final String TEST_VARIABLE_VALUE = "TEST_VAR_NAME";

    @Test
    public void shouldGetVariable_whenExists() {
        var mockVariables = Map.of(TEST_VARIABLE_NAME, TEST_VARIABLE_VALUE);
        var reader = new EnvironmentVariableReader(mockVariables::get, mockVariables::keySet);

        String actualValue = reader.readVariable(TEST_VARIABLE_NAME);

        assertEquals(TEST_VARIABLE_VALUE, actualValue);
    }

    @Test
    public void shouldThrowVariableNotFoundException_whenDoesNotExist() {
        Map<String, String> mockVariables = Map.of();
        var reader = new EnvironmentVariableReader(mockVariables::get, mockVariables::keySet);

        var exception = assertThrows(EnvironmentVariableNotFoundException.class, () -> {
            reader.readVariable(TEST_VARIABLE_NAME);
        });

        assertEquals(
                exception.getMessageTemplate().formatted(TEST_VARIABLE_NAME),
                exception.getMessage()
        );
    }

    @Test
    public void shouldGetAllVariableNames() {
        var mockVariables = Map.of(TEST_VARIABLE_NAME, TEST_VARIABLE_VALUE);
        var reader = new EnvironmentVariableReader(mockVariables::get, mockVariables::keySet);

        assertEquals(Set.of(TEST_VARIABLE_NAME), reader.getAllVariableNames());
    }

}
