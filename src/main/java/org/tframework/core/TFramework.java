/* Licensed under Apache-2.0 2022. */
package org.tframework.core;

import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.tframework.core.elements.PreConstructedElementData;
import org.tframework.core.initializers.CoreInitializationFactory;
import org.tframework.core.initializers.CoreInitializationInput;

/**
 * The core class of the framework, which includes methods to start and stop a TFramework
 * application.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TFramework {

    /**
     * Start a TFramework application. This is intended to be called from the {@code main} method. Simplified
     * version of {@link #start(String, Class, String[], Set)}.
     * @param applicationName The name of the application. This is an arbitrary name. Must not be null.
     * @param rootClass The root class of the application. This class and all others in its package and subpackages
     *                  will be scanned for elements. This will typically be the class that the {@code start}
     *                  method was called from. Must not be null.
     * @param args Command line arguments, as received in the {@code main} method. Must not be null.
     * @return The {@link Application} that was started.
     */
    public static Application start(
            @NonNull String applicationName,
            @NonNull Class<?> rootClass,
            @NonNull String[] args
    ) {
        return start(applicationName, rootClass, args, Set.of());
    }

    /**
     * Start a TFramework application. This is intended to be called from the {@code main} method. Provides all
     * customizations to start a TFramework app. For must use cases, the simplified {@link #start(String, Class, String[])}
     * is enough.
     * @param applicationName The name of the application. This is an arbitrary name. Must not be null.
     * @param rootClass The root class of the application. This class and all others in its package and subpackages
     *                  will be scanned for elements. This will typically be the class that the {@code start}
     *                  method was called from. Must not be null.
     * @param args Command line arguments, as received in the {@code main} method. Must not be null.
     * @param preConstructedElementData A collection of {@link PreConstructedElementData} which has information about
     *                                  objects that should be added to the elements.
     * @return The {@link Application} that was started.
     */
    public static Application start(
            @NonNull String applicationName,
            @NonNull Class<?> rootClass,
            @NonNull String[] args,
            @NonNull Set<PreConstructedElementData> preConstructedElementData
    ) {
        var initializerProcess = CoreInitializationFactory.createCoreInitializationProcess();
        var input = CoreInitializationInput.builder()
                .applicationName(applicationName)
                .rootClass(rootClass)
                .args(args)
                .preConstructedElementData(preConstructedElementData)
                .build();
        return initializerProcess.performCoreInitialization(input);
    }

    /**
     * Stops a TFramework application gracefully.
     * @param application The {@link Application} to stop.
     */
    public static void stop(Application application) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
