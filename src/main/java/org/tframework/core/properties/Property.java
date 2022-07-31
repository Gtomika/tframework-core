package org.tframework.core.properties;

import lombok.Getter;

/**
 * Holds a property name and value pair.
 * @param <T> The type of the property.
 */
public class Property<T> {

    /**
     * The property name.
     */
    @Getter
    private final String name;

    /**
     * The property value
     */
    @Getter
    private final T value;

    /**
     * Type of the property.
     */
    @Getter
    private final Class<T> propertyType;

    /**
     * Create property. The type will be determined automatically.
     */
    public Property(String name, T value) {
        this.name = name;
        this.value = value;
        this.propertyType = (Class<T>) value.getClass();
    }
}
