/*
Copyright 2024 Tamas Gaspar

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
