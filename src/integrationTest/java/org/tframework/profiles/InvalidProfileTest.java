/* Licensed under Apache-2.0 2024. */
package org.tframework.profiles;

import org.junit.jupiter.api.Test;
import org.tframework.core.profiles.InvalidProfileException;
import org.tframework.test.TframeworkAssertions;
import org.tframework.test.annotations.ExpectInitializationFailure;
import org.tframework.test.annotations.InjectInitializationException;
import org.tframework.test.annotations.IsolatedTFrameworkTest;
import org.tframework.test.annotations.SetProfiles;

@ExpectInitializationFailure
@SetProfiles(InvalidProfileTest.INVALID_PROFILE)
@IsolatedTFrameworkTest
public class InvalidProfileTest {

    public static final String INVALID_PROFILE = "";

    @Test
    public void shouldFailInitialization_whenIllegalProfileProvided(@InjectInitializationException Exception e) {
        TframeworkAssertions.assertInitializationExceptionWithCause(e, InvalidProfileException.class);
    }

}
