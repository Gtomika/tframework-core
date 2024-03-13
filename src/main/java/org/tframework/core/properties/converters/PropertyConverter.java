/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.converters;

import org.tframework.core.properties.PropertyValue;

/**
 * The property converter transforms the {@link PropertyValue} into the supported type.
 * @param <T> The type this converter produces.
 * @see PropertyConvertersFactory
 */
public interface PropertyConverter<T> {

    /**
     * Convert the given {@link PropertyValue} into the supported type.
     * @param propertyValue The property value to convert.
     * @return The converted value.
     * @throws PropertyConversionException If the conversion failed.
     */
    T convert(PropertyValue propertyValue);

    /**
     * @return The type this converter produces. If this converter should support primitive types as well,
     * the <b>wrapper</b> class should be returned here.
     */
    Class<T> getType();

}
