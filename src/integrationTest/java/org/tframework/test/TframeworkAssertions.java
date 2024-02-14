/* Licensed under Apache-2.0 2024. */
package org.tframework.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.opentest4j.AssertionFailedError;
import org.tframework.core.initializers.InitializationException;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.profiles.scanners.DefaultProfileScanner;

/**
 * Utilities for making assertions on TFramework applications.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TframeworkAssertions {

    /**
     * Assert that the application has exactly the provided profiles, and the
     * {@link DefaultProfileScanner#DEFAULT_PROFILE_NAME}.
     */
    public static void assertHasNonDefaultProfilesExactly(ProfilesContainer profilesContainer, Set<String> nonDefaultProfiles) {
        var expectedProfiles = new HashSet<>(nonDefaultProfiles);
        expectedProfiles.add(DefaultProfileScanner.DEFAULT_PROFILE_NAME);

        assertEquals(expectedProfiles, profilesContainer.profiles());
    }

    /**
     * Asserts that the exception is an {@link InitializationException} with the given type of ROOT cause.
     */
    public static void assertInitializationExceptionWithCause(Exception exception, Class<? extends Exception> expectedCauseType) {
        if(exception instanceof InitializationException) {
            Throwable actualCause = Stream.iterate(exception, Throwable::getCause)
                    .filter(element -> element.getCause() == null)
                    .findFirst()
                    .orElseThrow(() -> new AssertionFailedError("Initialization exception has no cause"));
            assertEquals(expectedCauseType, actualCause.getClass());
        } else {
            fail("Exception was not " + InitializationException.class.getName() + ", but " + exception.getClass().getName());
        }
    }

}
