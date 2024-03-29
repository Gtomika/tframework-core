/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import lombok.NoArgsConstructor;
import org.tframework.core.properties.extractors.PropertyExtractorsFactory;
import org.tframework.core.properties.parsers.PropertyParsersFactory;
import org.tframework.core.properties.yamlparsers.YamlParsersFactory;
import org.tframework.core.readers.ReadersFactory;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class PropertiesInitializationProcessFactory {

    /**
     * Creates {@link PropertiesInitializationProcess} that the framework uses to initialize properties.
     */
    public static PropertiesInitializationProcess createProfileInitializationProcess() {
        return PropertiesInitializationProcess.builder()
                .resourceFileReader(ReadersFactory.createResourceFileReader())
                .yamlParser(YamlParsersFactory.createDefaultYamlParser())
                .propertiesExtractor(PropertyExtractorsFactory.createPropertiesExtractor())
                .propertyParser(PropertyParsersFactory.createDefaultPropertyParser())
                .build();
    }

}
