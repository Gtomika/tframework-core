/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.scanners;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesInitializationInput;

/**
 * Utilities for creating {@link PropertyFileScanner}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyFileScannersFactory {

    /**
     * Creates a list of {@link PropertyFileScanner}s that will be used during framework initialization.
     * @param input {@link PropertiesInitializationInput} with all data required to create the scanners.
     */
    public static List<PropertyFileScanner> createTframeworkPropertyFileScanners(PropertiesInitializationInput input) {
        //the order of the scanners is important
        return List.of(
                createDefaultScanner(),
                createProfilesScanner(input.profilesContainer()),
                createCliArgumentScanner(input.cliArgs())
        );
    }

    private static DefaultPropertyFileScanner createDefaultScanner() {
        return new DefaultPropertyFileScanner();
    }

    private static ProfilesPropertyFileScanner createProfilesScanner(ProfilesContainer profilesContainer) {
        return new ProfilesPropertyFileScanner(profilesContainer);
    }

    private static CliArgumentPropertyFileScanner createCliArgumentScanner(String[] args) {
        return new CliArgumentPropertyFileScanner(args);
    }

}
