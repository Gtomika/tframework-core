/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.scanners;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.properties.PropertiesInitializationInput;
import org.tframework.core.readers.ReadersFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyScannersFactory {

    /**
     * Creates the default {@link PropertyScanner}s that the framework will use to
     * find directly specified properties.
     * @param input {@link PropertiesInitializationInput} with the required data to construct the scanners.
     */
    public static List<PropertyScanner> createDefaultPropertyScanners(PropertiesInitializationInput input) {
        return List.of( //order is not particularly important here
                createCliArgumentPropertyScanner(input.cliArgs()),
                createEnvironmentVariableScanner(),
                createSystemPropertyScanner()
        );
    }

    private static CliArgumentPropertyScanner createCliArgumentPropertyScanner(String[] args) {
        return new CliArgumentPropertyScanner(args);
    }

    private static SystemPropertyScanner createSystemPropertyScanner() {
        var reader = ReadersFactory.createSystemPropertyReader();
        return new SystemPropertyScanner(reader);
    }

    private static EnvironmentPropertyScanner createEnvironmentVariableScanner() {
        var reader = ReadersFactory.createEnvironmentVariableReader();
        return new EnvironmentPropertyScanner(reader);
    }

}
