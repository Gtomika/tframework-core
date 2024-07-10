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
package org.tframework.core.properties.extractors;

import java.util.List;
import java.util.Map;
import org.tframework.core.properties.Property;
import org.tframework.core.properties.PropertyValue;

/**
 * The properties extractor converts the "raw" result of a {@link org.tframework.core.properties.yamlparsers.YamlParser}
 * into the {@link PropertyValue} format (see {@link #extractProperties(Map)} for details).
 * @see PropertyExtractorsFactory
 */
public interface PropertiesExtractor {

    /**
     * Separator used when YAML elements are combined into properties. For example the YAML
     * <pre>{@code
     * a:
     *   b:
     *     c: demo
     * }</pre>
     * Will be combined into the property {@code a.b.c} (the separator being the {@code .} character),
     * with the value {@code demo}.
     */
    String PROPERTY_PATH_SEPARATOR = ".";

    /**
     * Extracts the properties map from given "raw" YAML parsing result.
     * @param parsedYaml Raw YAML parsing result produced by a {@link org.tframework.core.properties.yamlparsers.YamlParser}.
     * @return List of {@link Property} objects extracted from the raw result.
     */
    List<Property> extractProperties(Map<String, Object> parsedYaml);

}
