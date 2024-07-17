package org.tframework.core.properties.placeholders;

import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.Property;
import org.tframework.core.properties.PropertyValue;

/**
 * The property placeholder resolver is a post processor for properties. It will replace placeholders in the property
 * values with different things, depending on the implementation. This layer does not know about {@link Property} or
 * {@link PropertyValue} objects, it only works with the raw property values.
 */
public interface PropertyPlaceholderResolver {

    /**
     * Resolves placeholders in the given property value.
     * @param propertyValue The property value to resolve the placeholders in.
     * @param propertiesContainer The {@link PropertiesContainer} that contains all properties. Some
     *                            resolvers may need to look up other properties to resolve the placeholders.
     * @return The property with the placeholders resolved.
     */
    String resolvePlaceholders(String propertyValue, PropertiesContainer propertiesContainer);

}
