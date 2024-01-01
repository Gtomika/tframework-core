/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.TFrameworkInternal;
import org.tframework.core.profiles.ProfileInitializationProcessFactory;
import org.tframework.core.properties.PropertiesInitializationProcessFactory;

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
        var propertiesCoreInitializer = createPropertiesCoreInitializer();
        return new CoreInitializationProcess(
                profilesCoreInitializer,
                propertiesCoreInitializer
        );
    }

    private static ProfilesCoreInitializer createProfilesCoreInitializer() {
        var profilesProcess = ProfileInitializationProcessFactory.createProfileInitializationProcess();
        return new ProfilesCoreInitializer(profilesProcess);
    }

    private static PropertiesCoreInitializer createPropertiesCoreInitializer() {
        var propertiesProcess = PropertiesInitializationProcessFactory.createProfileInitializationProcess();
        return new PropertiesCoreInitializer(propertiesProcess);
    }

}
