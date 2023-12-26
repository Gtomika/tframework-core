/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.TFrameworkInternal;
import org.tframework.core.profiles.ProfileInitializationProcessFactory;

/**
 * Factory methods to instantiate {@link CoreInitializer}s and the {@link CoreInitializationProcess}.
 */
@TFrameworkInternal
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CoreInitializationFactory {

    /**
     * Creates a {@link CoreInitializationProcess} that performs framework initialization.
     */
    public static CoreInitializationProcess createCoreInitializationProcess() {
        var profilesCoreInitializer = createProfilesCoreInitializer();
        return new CoreInitializationProcess(profilesCoreInitializer);
    }

    private static ProfilesCoreInitializer createProfilesCoreInitializer() {
        var profilesProcess = ProfileInitializationProcessFactory.createProfileInitializationProcess();
        return new ProfilesCoreInitializer(profilesProcess);
    }

}
