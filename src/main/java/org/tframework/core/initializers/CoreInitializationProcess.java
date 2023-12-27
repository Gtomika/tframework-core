/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.Application;
import org.tframework.core.TFrameworkInternal;
import org.tframework.core.profiles.ProfileInitializationInput;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.utils.TimerUtils;

/**
 * The process that bootstraps the framework is described by this class.
 */
@Slf4j
@TFrameworkInternal
@RequiredArgsConstructor
public class CoreInitializationProcess {

    private final ProfilesCoreInitializer profilesInitializer;

    /**
     * Perform the core initialization.
     * @param coreInput {@link CoreInitializationInput} with all data required to initialize.
     * @return {@link CoreInitializationResult} with initialization results, if it was successful.
     * @throws InitializationException If the process failed and the application cannot be started.
     */
    public CoreInitializationResult performCoreInitialization(CoreInitializationInput coreInput) {
        log.info("Starting TFramework core initialization...");
        Instant start = Instant.now();

        try {
            ProfilesContainer profilesContainer = initProfiles(coreInput);

            Application application = Application.builder()
                    .profilesContainer(profilesContainer)
                    .build();
            return new CoreInitializationResult(application);
        } catch (Exception e) {
            log.error("The core initialization has failed", e);
            throw new InitializationException(e);
        } finally {
            log.info("The core initialization completed in {} ms.", TimerUtils.msBetween(start, Instant.now()));
        }
    }

    private ProfilesContainer initProfiles(CoreInitializationInput coreInput) {
        var profileInput = ProfileInitializationInput.builder()
                .args(coreInput.args())
                .build();
        return profilesInitializer.initialize(profileInput);
    }

}
