/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.PreConstructedElement;
import org.tframework.core.properties.converters.PropertyConversionException;
import org.tframework.core.properties.converters.PropertyConverterAggregator;

/**
 * A read-only container of the properties, and related methods to access them.
 */
@Slf4j
@EqualsAndHashCode
@PreConstructedElement
public final class PropertiesContainer {

    private final List<Property> properties;

    @Setter
    private PropertyConverterAggregator propertyConverterAggregator;

    /**
     * Creates a container from the specified properties. The {@link PropertyConverterAggregator} will be set
     * later, when the converter elements are available.
     */
    public PropertiesContainer(List<Property> properties) {
        this.properties = properties;
    }

    /**
     * Creates a container from the specified properties using a given {@link PropertyConverterAggregator}.
     * This is mainly used for testing purposes and copying.
     */
    public PropertiesContainer(List<Property> properties, PropertyConverterAggregator propertyConverterAggregator) {
        this.properties = properties;
        this.propertyConverterAggregator = propertyConverterAggregator;
    }

    /**
     * Gets the {@link PropertyValue} of a property.
     * @return The {@link PropertyValue} for the given property name.
     * @throws PropertyNotFoundException If the property does not exist.
     */
    public PropertyValue getPropertyValueObject(String propertyName) {
        return PropertyUtils.getValueFromPropertyList(properties, propertyName);
    }

    /**
     * Gets the {@link PropertyValue} of a property, or the provided default one.
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
     * Gets a property by name, and converts it to the desired type.
     * @param propertyName The property to get.
     * @param requiredType The type to convert this property to.
     * @throws PropertyNotFoundException If the property does not exist.
     * @throws PropertyConversionException If the conversion failed to the required type.
     */
    public <T> T getPropertyValue(String propertyName, Class<T> requiredType) {
        var propertyValueObject = getPropertyValueObject(propertyName);
        return propertyConverterAggregator.convert(propertyValueObject, requiredType);
    }

    /**
     * A version of {@link #getPropertyValue(String, Class)} that returns a default value instead of throwing
     * {@link PropertyNotFoundException} when the given property does not exist.
     */
    public <T> T getPropertyValue(String propertyName, Class<T> requiredType, T defaultValue) {
        try {
            return getPropertyValue(propertyName, requiredType);
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
        return new PropertiesContainer(mergedProperties, propertyConverterAggregator);
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
}
