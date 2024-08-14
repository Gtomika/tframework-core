/*
Copyright 2022 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
     *                  method was called from. Must not be null. Must be annotated with {@link TFrameworkRootClass}.
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
     *                  method was called from. Must not be null. Must be annotated with {@link TFrameworkRootClass}.
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
     * Stops a TFramework application gracefully. This is simply a wrapper
     * around {@link Application#close()}.
     * @param application The {@link Application} to stop.
     */
    public static void stop(Application application) {
        application.close();
    }
}
