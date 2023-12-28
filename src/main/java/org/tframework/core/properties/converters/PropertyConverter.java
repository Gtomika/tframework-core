/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.converters;

import org.tframework.core.properties.PropertyValue;

/**
 * The property converter transforms the {@link PropertyValue} into the supported type.
 * @param <T> The type this converter produces.
 */
public abstract class PropertyConverter<T> {

    /**
     * Convert the given {@link PropertyValue} into the supported type.
     * @param propertyValue The property value to convert.
     * @return The converted value.
     * @throws PropertyConversionException If the conversion failed.
     */
    public final T convert(PropertyValue propertyValue) {
        try {
            return convertInternal(propertyValue);
        } catch (Exception e) {
            throw PropertyConversionException.builder()
                    .propertyValue(propertyValue)
                    .type(getType())
                    .cause(e)
                    .build();
        }
    }

    /**
     * Convert the given {@link PropertyValue} into the supported type.
     * @throws Exception If the conversion failed. Any type of exception may be thrown: should not be
     * {@link PropertyConversionException} as this is handled by the caller.
     */
    protected abstract T convertInternal(PropertyValue propertyValue) throws Exception;

    /**
     * @return The type this converter produces.
     */
    public abstract Class<T> getType();

}
