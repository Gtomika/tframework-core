/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles.scanners;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.profiles.ProfileInitializationInput;
import org.tframework.core.readers.ReadersFactory;

/**
 * Utilities for creating {@link ProfileScanner}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileScannersFactory {

    /**
     * Creates a {@link List} of {@link ProfileScanner}s that are used by the framework to detect profiles
     * during initialization.
     * @param input {@link ProfileInitializationInput} required to create these scanners.
     */
    public static List<ProfileScanner> tframeworkProfileScanners(ProfileInitializationInput input) {
        return List.of(
                createDefaultProfileScanner(),
                createEnvironmentProfileScanner(),
                createSystemPropertyProfileScanner(),
                createCliProfileScanner(input.args())
        );
    }

    private static EnvironmentProfileScanner createEnvironmentProfileScanner() {
        var environmentVariableReader = ReadersFactory.createEnvironmentVariableReader();
        return new EnvironmentProfileScanner(environmentVariableReader);
    }

    private static SystemPropertyProfileScanner createSystemPropertyProfileScanner() {
        var systemPropertyReader = ReadersFactory.createSystemPropertyReader();
        return new SystemPropertyProfileScanner(systemPropertyReader);
    }

    private static DefaultProfileScanner createDefaultProfileScanner() {
        return new DefaultProfileScanner();
    }

    private static CLIProfileScanner createCliProfileScanner(String[] args) {
        return new CLIProfileScanner(args);
    }

}
