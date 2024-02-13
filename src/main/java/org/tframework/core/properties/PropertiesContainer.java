/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.PreConstructedElement;
import org.tframework.core.elements.dependency.DependencySource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A read-only container of the properties, and related methods to access them.
 */
@Slf4j
@EqualsAndHashCode
@PreConstructedElement
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertiesContainer implements DependencySource {

    private final List<Property> properties;

    /**
     * Gets the {@link PropertyValue} of a property. If interested only in the raw underlying value,
     * it's possible to use {@link #getPropertyValue(String)} or {@link #getPropertyValueList(String)} instead.
     * @return The {@link PropertyValue} for the given property name.
     * @throws PropertyNotFoundException If the property does not exist.
     */
    public PropertyValue getPropertyValueObject(String propertyName) {
        return PropertyUtils.getValueFromPropertyList(properties, propertyName);
    }

    /**
     * Gets the {@link PropertyValue} of a property, or the provided default one. If interested only in the raw underlying value,
     * it's possible to use {@link #getPropertyValue(String)} or {@link #getPropertyValueList(String)} instead.
     * @param propertyName The property to get.
     * @param defaultValue A default value to return if the property does not exist.
     */
    public PropertyValue getPropertyValueObject(String propertyName, PropertyValue defaultValue) {
        try {
            return PropertyUtils.getValueFromPropertyList(properties, propertyName);
        } catch (PropertyNotFoundException e) {
            log.debug("Property '{}' not found. Returning default value '{}'", propertyName, defaultValue);
            return defaultValue;
        }
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
    public PropertiesContainer merge(@NonNull List<Property> additionalProperties) {
        List<Property> mergedProperties = new ArrayList<>(properties);
        for(var property: additionalProperties) {
            var newValue = property.value();
            try {
                var oldValue = PropertyUtils.getValueFromPropertyList(mergedProperties, property.name());
                log.debug("Overriding property '{}'. Old value: '{}'. New value: '{}'", property.name(), oldValue, newValue);
                PropertyUtils.replaceValueInPropertyList(mergedProperties, property);
            } catch (PropertyNotFoundException e) {
                mergedProperties.add(property);
            }
        }
        return PropertiesContainer.fromProperties(mergedProperties);
    }

    /**
     * Exposes all the names of the properties in this container.
     */
    public List<String> propertyNames() {
        return properties.stream()
                .map(Property::name)
                .toList();
    }

    /**
     * Creates a new {@link PropertiesContainer} with the original ones merged with the ones found in
     * the other container. Properties in the other container will override the current ones.
     * @param otherContainer Non-null container to merge into this one.
     * @return A new container with the merged properties.
     */
    public PropertiesContainer merge(@NonNull PropertiesContainer otherContainer) {
        return merge(otherContainer.properties);
    }

    /**
     * Creates a well-formed {@link String} representation of this {@link PropertiesContainer} which lists
     * the properties in alphabetical order.
     */
    @Override
    public String toString() {
        String containerString = "Properties container with the following properties:\n";
        containerString += properties.stream()
                .sorted()
                .map(property -> " - " + property.toString())
                .collect(Collectors.joining("\n"));
        return containerString;
    }

    /**
     * Creates a {@link PropertiesContainer} from the given list of properties.
     * @param properties Properties list to create the container from, cannot be null.
     */
    public static PropertiesContainer fromProperties(@NonNull List<Property> properties) {
        return new PropertiesContainer(properties);
    }

    /**
     * Creates an empty {@link PropertiesContainer}.
     */
    public static PropertiesContainer empty() {
        return fromProperties(List.of());
    }

}
