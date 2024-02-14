/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProfileValidatorTest {

    private final ProfileValidator profileValidator = new ProfileValidator();

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "???", "üö"})
    public void shouldValidateProfile_andThrowException_ifInvalid(String profile) {
        var exception = assertThrows(InvalidProfileException.class, () -> profileValidator.validate(profile));

        String expectedProfileInException = profile == null ? "null" : profile;
        assertEquals(
                exception.getMessageTemplate().formatted(expectedProfileInException, ProfileValidator.class.getName()),
                exception.getMessage()
        );
    }

    @Test
    public void shouldValidateProfile_andThrowException_ifTooLong() {
        String longProfile = "a".repeat(ProfileValidator.MAX_PROFILE_LENGTH + 10);
        var exception = assertThrows(InvalidProfileException.class, () -> profileValidator.validate(longProfile));
        assertEquals(
                exception.getMessageTemplate().formatted(longProfile, ProfileValidator.class.getName()),
                exception.getMessage()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"DEV", "prod-db", "123-456", "My_BEST_profile-1"})
    public void shouldValidateProfile_andAcceptIt_ifValid(String profile) {
        assertDoesNotThrow(() -> profileValidator.validate(profile));
    }

}
