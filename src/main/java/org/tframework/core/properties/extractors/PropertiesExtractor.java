/* Licensed under Apache-2.0 2023. */
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
