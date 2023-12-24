/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ProfileValidatorTest {

	private final ProfileValidator profileValidator = new ProfileValidator();

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "ABC", "üö", "123-456"})
	public void shouldValidateProfile_andThrowException_ifNull(String profile) {
		var exception = assertThrows(InvalidProfileException.class, () -> profileValidator.validate(profile));

		String expectedProfileInException = profile == null ? "null" : profile;
		assertEquals(
				exception.getMessageTemplate().formatted(expectedProfileInException, ProfileValidator.class.getName()),
				exception.getMessage()
		);
	}

	@ParameterizedTest
	@ValueSource(strings = {"dev", "prod-db", "test"})
	public void shouldValidateProfile_andAcceptIt_ifValid(String profile) {
		assertDoesNotThrow(() -> profileValidator.validate(profile));
	}

}
