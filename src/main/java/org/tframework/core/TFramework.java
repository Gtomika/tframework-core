/* Licensed under Apache-2.0 2022. */
package org.tframework.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.initializers.CoreInitializationFactory;
import org.tframework.core.initializers.CoreInitializationInput;

/**
 * The core class of the framework, which includes methods to start and stop a TFramework
 * application.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TFramework {

    /**
     * Start a TFramework application. This is intended to be called from the {@code main} method.
     * @param args Command line arguments, as received in the {@code main} method.
     * @return The {@link Application} that was started.
     */
    public static Application start(String[] args) {
        var initializerProcess = CoreInitializationFactory.createCoreInitializationProcess();
        var input = CoreInitializationInput.builder()
                .args(args)
                .build();
        return initializerProcess.performCoreInitialization(input);
    }

    /**
     * Stops a TFramework application gracefully.
     * @param application The {@link Application} to stop.
     */
    public static void stop(Application application) {
        //TODO: is this necessary?
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
