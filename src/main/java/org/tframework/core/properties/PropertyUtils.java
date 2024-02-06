package org.tframework.core.properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

/**
 * Utility methods for properties, such as working with lists
 * of {@link Property} objects.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyUtils {

    /**
     * Gets a properties value from a list of properties.
     * @param properties Non-null list of {@link Property} objects.
     * @param name Non-null name of the property to get from the list.
     * @return {@link PropertyValue} that belongs to the property with this given name.
     * @throws PropertyNotFoundException If there is no property in the list with the given name.
     */
    public static PropertyValue getValueFromPropertyList(
            @NonNull List<Property> properties,
            @NonNull String name
    ) throws PropertyNotFoundException {
        return properties.stream()
                .filter(property -> property.name().equals(name))
                .findFirst()
                .map(Property::value)
                .orElseThrow(() -> new PropertyNotFoundException(name));
    }

    /**
     * Replaces a property in a property list.
     * @param properties List to make the replacement in.
     * @param newProperty A {@link Property} that should be added to this list. If a property already exists which
     *                    has the same name, that will be removed.
     */
    public static void replaceValueInPropertyList(@NonNull List<Property> properties, @NonNull Property newProperty) {
        properties.removeIf(property -> property.name().equals(newProperty.name()));
        properties.add(newProperty);
    }

}
