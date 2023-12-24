/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers.core;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.Application;
import org.tframework.core.profiles.ProfileInitializationInput;
import org.tframework.core.profiles.Profiles;
import org.tframework.core.utils.TimerUtils;

/**
 * The process that bootstraps the framework is described by this class.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE) //for tests only
public class CoreInitializationProcess {

    private final ProfilesCoreInitializer profilesInitializer;

    public CoreInitializationProcess() {
        profilesInitializer = new ProfilesCoreInitializer();
    }

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
            //initialize the profiles, they are required by following steps
            var profileInitializationInput = new ProfileInitializationInput(coreInput.args());
            Profiles profiles = profilesInitializer.initialize(profileInitializationInput);

            Application application = Application.builder()
                    .profiles(profiles)
                    .build();
            return new CoreInitializationResult(application);
        } catch (Exception e) {
            log.error("The core initialization has failed", e);
            throw new InitializationException(e);
        } finally {
            log.info("The core initialization completed in {} ms.", TimerUtils.msBetween(start, Instant.now()));
        }
    }

}
