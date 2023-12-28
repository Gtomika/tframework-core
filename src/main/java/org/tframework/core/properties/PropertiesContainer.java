/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * A read-only container of the properties, and related utility methods.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertiesContainer {

    private final Map<String, ? extends PropertyValue> properties;

    /**
     * @return The {@link PropertyValue} for the given property name.
     * @throws PropertyNotFoundException If the property does not exist.
     */
    public PropertyValue getPropertyValue(String propertyName) {
        return Optional.ofNullable(properties.get(propertyName))
                .orElseThrow(() -> new PropertyNotFoundException(propertyName));
    }

    /**
     * Creates a {@link PropertiesContainer} from the given {@link Map} of properties.
     * @param properties Properties map, usually produced by a {@link org.tframework.core.properties.extractors.PropertiesExtractor}.
     */
    public static PropertiesContainer fromProperties(Map<String, ? extends PropertyValue> properties) {
        return new PropertiesContainer(properties);
    }

}
