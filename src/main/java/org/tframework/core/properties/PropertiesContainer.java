/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A read-only container of the properties, and related utility methods.
 */
@Slf4j
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertiesContainer {

    private final Map<String, PropertyValue> properties;

    /**
     * @return The {@link PropertyValue} for the given property name.
     * @throws PropertyNotFoundException If the property does not exist.
     */
    public PropertyValue getPropertyValue(String propertyName) {
        return Optional.ofNullable(properties.get(propertyName))
                .orElseThrow(() -> new PropertyNotFoundException(propertyName));
    }

    /**
     * @return How many properties are in this container.
     */
    public int size() {
        return properties.size();
    }

    /**
     * Creates a new {@link PropertiesContainer} with the original properties merged with the
     * given additional properties. {@code additionalProperties} will override the original ones.
     * @param additionalProperties New properties to add to the current ones.
     * @return A new {@link PropertiesContainer} with the merged properties.
     */
    PropertiesContainer merge(@NonNull Map<String, PropertyValue> additionalProperties) {
        Map<String, PropertyValue> mergedProperties = new HashMap<>(properties);
        for(String propertyName : additionalProperties.keySet()) {
            var newValue = additionalProperties.get(propertyName);
            if(mergedProperties.containsKey(propertyName)) {
                var oldValue = mergedProperties.get(propertyName);
                log.debug("Overriding property '{}'. Old value: '{}'. New value: '{}'", propertyName, oldValue, newValue);
            }
            mergedProperties.put(propertyName, newValue);
        }
        return PropertiesContainer.fromProperties(mergedProperties);
    }

    /**
     * Creates a well-formed {@link String} representation of this {@link PropertiesContainer} which lists
     * the properties in alphabetical order.
     */
    @Override
    public String toString() {
        var propertyNames = properties.keySet()
                .stream()
                .sorted()
                .toList();
        StringBuilder stringBuilder = new StringBuilder("Properties container with the following properties:\n");
        for(String propertyName : propertyNames) {
            stringBuilder.append(" - ")
                    .append(propertyName)
                    .append(": ")
                    .append(properties.get(propertyName))
                    .append("\n");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1); // Remove the last \n
        return stringBuilder.toString();
    }

    /**
     * Creates a {@link PropertiesContainer} from the given {@link Map} of properties.
     * @param properties Properties map, usually produced by a {@link org.tframework.core.properties.extractors.PropertiesExtractor}.
     */
    public static PropertiesContainer fromProperties(Map<String, PropertyValue> properties) {
        return new PropertiesContainer(properties);
    }

    /**
     * Creates an empty {@link PropertiesContainer}.
     */
    public static PropertiesContainer empty() {
        return fromProperties(Map.of());
    }

}
