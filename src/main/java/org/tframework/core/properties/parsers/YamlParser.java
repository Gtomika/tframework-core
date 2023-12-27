/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.parsers;

import java.util.Map;

/**
 * A YAML parser converts a YAML string into a {@link Map} of contents, but does
 * not perform further post-processing on the parsed map.
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
