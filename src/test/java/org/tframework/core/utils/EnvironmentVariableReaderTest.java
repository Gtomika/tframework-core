/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.Test;

class EnvironmentVariableReaderTest {

	private static final String TEST_VARIABLE_NAME = "TEST_VAR";
	private static final String TEST_VARIABLE_VALUE = "TEST_VAR_NAME";

	@Test
	public void shouldGetVariable_whenExists() {
		var mockVariables = Map.of(TEST_VARIABLE_NAME, TEST_VARIABLE_VALUE);
		var reader = new EnvironmentVariableReader(mockVariables::get);

		String actualValue = reader.readVariable(TEST_VARIABLE_NAME);

		assertEquals(TEST_VARIABLE_VALUE, actualValue);
	}

	@Test
	public void shouldThrowVariableNotFoundException_whenDoesNotExist() {
		Map<String, String> mockVariables = Map.of();
		var reader = new EnvironmentVariableReader(mockVariables::get);

		var exception = assertThrows(EnvironmentVariableNotFoundException.class, () -> {
			reader.readVariable(TEST_VARIABLE_NAME);
		});

		assertEquals(
				exception.getMessageTemplate().formatted(TEST_VARIABLE_NAME),
				exception.getMessage()
		);
	}

}
