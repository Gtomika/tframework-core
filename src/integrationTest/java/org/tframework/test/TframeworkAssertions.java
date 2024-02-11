/* Licensed under Apache-2.0 2024. */
package org.tframework.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.opentest4j.AssertionFailedError;
import org.tframework.core.Application;
import org.tframework.core.initializers.InitializationException;
import org.tframework.core.profiles.scanners.DefaultProfileScanner;

/**
 * Utilities for making assertions on TFramework applications.
 */
@RequiredArgsConstructor
public class TframeworkAssertions {

    private final Application application;

    /**
     * Assert that the application has exactly the provided profiles, and the
     * {@link DefaultProfileScanner#DEFAULT_PROFILE_NAME}.
     */
    public void assertHasNonDefaultProfilesExactly(Set<String> nonDefaultProfiles) {
        var expectedProfiles = new HashSet<>(nonDefaultProfiles);
        expectedProfiles.add(DefaultProfileScanner.DEFAULT_PROFILE_NAME);

        assertEquals(expectedProfiles, application.getProfilesContainer().profiles());
    }

    /**
     * Asserts that the exception is an {@link InitializationException} with the given type of ROOT cause.
     */
    public void assertInitializationExceptionWithCause(Exception exception, Class<? extends Exception> expectedCauseType) {
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

    public static TframeworkAssertions failedApplicationAssertions() {
        return new TframeworkAssertions(null);
    }

}
