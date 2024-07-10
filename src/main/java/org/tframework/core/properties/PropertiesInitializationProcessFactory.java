/*
Copyright 2023 Tamas Gaspar

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
