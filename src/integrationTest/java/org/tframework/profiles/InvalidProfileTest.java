/* Licensed under Apache-2.0 2024. */
package org.tframework.profiles;

import org.junit.jupiter.api.Test;
import org.tframework.core.profiles.InvalidProfileException;
import org.tframework.test.TframeworkAssertions;
import org.tframework.test.annotations.InjectInitializationException;
import org.tframework.test.annotations.IsolatedTFrameworkTest;
import org.tframework.test.annotations.SetProfiles;

@SetProfiles("") //empty profile is not allowed
@IsolatedTFrameworkTest
public class InvalidProfileTest {

    @Test
    public void shouldFailInitialization_whenIllegalProfileProvided(@InjectInitializationException Exception e) {
        var frameworkAssertions = TframeworkAssertions.failedApplicationAssertions();
        frameworkAssertions.assertInitializationExceptionWithCause(e, InvalidProfileException.class);
    }

}
