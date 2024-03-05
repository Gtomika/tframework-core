/* Licensed under Apache-2.0 2024. */
package org.tframework.profiles;

import org.junit.jupiter.api.Test;
import org.tframework.core.profiles.InvalidProfileException;
import org.tframework.test.commons.annotations.ExpectInitializationFailure;
import org.tframework.test.commons.annotations.InjectInitializationException;
import org.tframework.test.commons.annotations.SetProfiles;
import org.tframework.test.commons.utils.TframeworkAssertions;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

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
