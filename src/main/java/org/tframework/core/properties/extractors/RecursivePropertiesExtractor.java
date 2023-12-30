/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.tframework.core.properties.PropertyValue;
import org.tframework.core.properties.extractors.leaves.LeafExtractor;

/**
 * A {@link PropertiesExtractor} implementation which walks the given parsed YAML
 * recursively to gather the properties.
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RecursivePropertiesExtractor implements PropertiesExtractor {

    private static final String ROOT_PROPERTY_PATH = "";

    private final List<LeafExtractor> leafExtractors;

    @Override
    public Map<String, PropertyValue> extractProperties(Map<String, Object> parsedYaml) {
        Map<String, PropertyValue> properties = new HashMap<>();
        saveProperties(properties, ROOT_PROPERTY_PATH, parsedYaml);
        return properties;
    }

    private void saveProperties(
            Map<String, PropertyValue> properties,
            String parentPropertyPath,
            Map<String, Object> parentNode
    ) {
        for(var entry: parentNode.entrySet()) {
            String propertyPath = entry.getKey();
            Object node = entry.getValue();

            String newPropertyPath = concatPropertyPath(parentPropertyPath, propertyPath);
            if(node instanceof Map<?, ?>) { // node is a Map, but not necessarily Map<String, Object>...
                saveProperties(properties, newPropertyPath, (Map<String, Object>) node);
            } else { //this node is a leaf
                properties.put(newPropertyPath, extractLeafValue(node));
            }
        }
    }

    private String concatPropertyPath(String parentPropertyPath, String propertyPath) {
        if(parentPropertyPath.equals(ROOT_PROPERTY_PATH)) {
            return propertyPath;
        }
        return parentPropertyPath + PROPERTY_PATH_SEPARATOR + propertyPath;
    }

    private PropertyValue extractLeafValue(Object node) {
        for(LeafExtractor leafExtractor : leafExtractors) {
            if(leafExtractor.matchesLeaf(node)) {
                return leafExtractor.extractLeaf(node);
            }
        }
        //should not get here, DefaultLeafExtractor should match anything
        throw new IllegalStateException("No leaf extractor found for node: " + node);
    }
}
