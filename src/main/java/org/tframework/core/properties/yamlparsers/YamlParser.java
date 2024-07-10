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
package org.tframework.core.properties.yamlparsers;

import java.util.Map;

/**
 * A YAML parser converts a YAML string into a {@link Map} of contents, but does
 * not perform further post-processing on the parsed map (see {@link #parseYaml(String)} for details).
 * @see YamlParsersFactory
 */
public interface YamlParser {

    /**
     * Read the given YAML string into a {@link Map}.
     * @param yaml Valid YAML string.
     * @return Map where the keys are the top level YAML elements, and the values are whatever these
     * elements have. These may be strings, numbers, lists, or nested maps.
     * @throws YamlParsingException If the parsing failed.
     */
    Map<String, Object> parseYaml(String yaml);

}
