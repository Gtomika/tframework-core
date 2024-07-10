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
package org.tframework.core.profiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

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
