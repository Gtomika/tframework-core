/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility methods to create the {@link ProfileInitializationProcess} that the framework will use
 * during startup.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileInitializationProcessFactory {

    /**
     * Creates the {@link ProfileInitializationProcess} used during startup.
     */
    public static ProfileInitializationProcess createProfileInitializationProcess() {
        var profileCleaner = new ProfileCleaner();
        var profileValidator = new ProfileValidator();

        return new ProfileInitializationProcess(
                profileCleaner,
                profileValidator
        );
    }

}
