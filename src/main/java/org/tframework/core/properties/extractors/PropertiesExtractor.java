/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors;

import java.util.Map;
import org.tframework.core.properties.PropertyValue;

/**
 * The properties extractor converts the "raw" result of a {@link org.tframework.core.properties.parsers.YamlParser}
 * into the {@link PropertyValue} format.
 */
public interface PropertiesExtractor {

    Map<String, PropertyValue> extractProperties(Map<String, Object> parsedYaml);

}
