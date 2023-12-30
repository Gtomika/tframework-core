/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import lombok.NoArgsConstructor;
import org.tframework.core.properties.extractors.PropertyExtractorsFactory;
import org.tframework.core.properties.parsers.YamlParsersFactory;
import org.tframework.core.properties.scanners.PropertyFileScannersFactory;
import org.tframework.core.readers.ReadersFactory;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class PropertiesInitializationProcessFactory {

    public static PropertiesInitializationProcess createProfileInitializationProcess(PropertiesInitializationInput input) {
        return PropertiesInitializationProcess.builder()
                .propertyFileScanners(PropertyFileScannersFactory.createTframeworkPropertyFileScanners(input))
                .resourceFileReader(ReadersFactory.createResourceFileReader())
                .yamlParser(YamlParsersFactory.createAvailableYamlParser())
                .propertiesExtractor(PropertyExtractorsFactory.createPropertiesExtractor())
                .build();
    }

}
