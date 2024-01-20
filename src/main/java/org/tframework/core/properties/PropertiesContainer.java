/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.dependency.DependencySource;

/**
 * A read-only container of the properties, and related utility methods.
 */
@Slf4j
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertiesContainer implements DependencySource {

    private final Map<String, PropertyValue> properties;

    /**
     * Gets the {@link PropertyValue} of a property. If interested only in the raw underlying value,
     * it's possible to use {@link #getPropertyValue(String)} or {@link #getPropertyValueList(String)} instead.
     * @return The {@link PropertyValue} for the given property name.
     * @throws PropertyNotFoundException If the property does not exist.
     */
    public PropertyValue getPropertyValueObject(String propertyName) {
        return Optional.ofNullable(properties.get(propertyName))
                .orElseThrow(() -> new PropertyNotFoundException(propertyName));
    }

    /**
     * Gets the {@link PropertyValue} of a property, or the provided default one. If interested only in the raw underlying value,
     * it's possible to use {@link #getPropertyValue(String)} or {@link #getPropertyValueList(String)} instead.
     * @param propertyName The property to get.
     * @param defaultValue A default value to return if the property does not exist.
     */
    public PropertyValue getPropertyValueObject(String propertyName, PropertyValue defaultValue) {
        return Optional.ofNullable(properties.get(propertyName))
                .orElseGet(() -> {
                    log.debug("Property '{}' not found. Returning default value '{}'", propertyName, defaultValue);
                    return defaultValue;
                });
    }

    /**
     * Convenience method that gets a property as a string. If the property is a list,
     * it will be converted to a string using {@code List#toString()}.
     * @throws PropertyNotFoundException If the property does not exist.
     */
    public String getPropertyValue(String propertyName) {
        return switch (getPropertyValueObject(propertyName)) {
            case SinglePropertyValue(var value) -> value;
            case ListPropertyValue(var values) -> values.toString();
        };
    }

    /**
     * A version of {@link #getPropertyValue(String)} that returns a default value instead of throwing
     * {@link PropertyNotFoundException} when the given property does not exist.
     */
    public String getPropertyValue(String propertyName, String defaultValue) {
        try {
            return getPropertyValue(propertyName);
        } catch (PropertyNotFoundException e) {
            log.debug("Property '{}' not found. Returning default value '{}'", propertyName, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Convenience method that gets a property as a list of strings. If this property is just a
     * single value, it will be converted to a list with a single element.
     * @throws PropertyNotFoundException If the property does not exist.
     */
    public List<String> getPropertyValueList(String propertyName) {
        return switch (getPropertyValueObject(propertyName)) {
            case SinglePropertyValue(var value) -> {
                log.debug("Property '{}' is a single value. Converting it to a list with a single element", propertyName);
                //List.of does not accept null, so we need to use ArrayList
                List<String> singleValueList = new ArrayList<>();
                singleValueList.add(value);
                yield singleValueList;
            }
            case ListPropertyValue(var values) -> values;
        };
    }

    /**
     * A version of {@link #getPropertyValueList(String)} that returns a default value instead of throwing
     * {@link PropertyNotFoundException} when the given property does not exist.
     */
    public List<String> getPropertyValueList(String propertyName, List<String> defaultValue) {
        try {
            return getPropertyValueList(propertyName);
        } catch (PropertyNotFoundException e) {
            log.debug("Property '{}' not found. Returning default value '{}'", propertyName, defaultValue);
            return defaultValue;
        }
    }

    /**
     * @return How many properties are in this container.
     */
    public int size() {
        return properties.size();
    }

    /**
     * Requests a property dependency from this container.
     * @param dependencyName The name of the dependency to request.
     * @return A {@link PropertyValue} with the requested property.
     * @throws PropertyNotFoundException If no property with the given name is found.
     */
    @Override
    public Object requestDependency(String dependencyName) {
        return getPropertyValueObject(dependencyName);
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
