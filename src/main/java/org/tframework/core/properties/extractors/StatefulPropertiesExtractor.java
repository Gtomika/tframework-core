/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors;

import java.util.Map;
import org.tframework.core.properties.PropertyValue;

/**
 * A stateful, non-thread-safe {@link PropertiesExtractor} implementation.
 */
public class StatefulPropertiesExtractor implements PropertiesExtractor {

    @Override
    public Map<String, PropertyValue> extractProperties(Map<String, Object> parsedYaml) {
        return null;
    }
}
