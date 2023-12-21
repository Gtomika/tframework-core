/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;
import org.junit.jupiter.api.Test;
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

    @Test
    public void shouldValidateProfiles_andThrowException_ifOneIsInvalid() {
        var profiles = Set.of("dev", "active-cache", "active database");
        var invalidProfiles = Set.of("active database");

        var exception = assertThrows(InvalidProfileException.class, () -> profileValidator.validateAll(profiles));

        assertEquals(
                exception.getMessageTemplate().formatted(invalidProfiles, ProfileValidator.class.getName()),
                exception.getMessage()
        );
    }

    @Test
    public void shouldValidateProfiles_andAcceptThem_ifAllValid() {
        var profiles = Set.of("dev", "active-cache", "test");
        assertDoesNotThrow(() -> profileValidator.validateAll(profiles));
    }

}