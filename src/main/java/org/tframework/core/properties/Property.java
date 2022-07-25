package org.tframework.core.properties;

import lombok.Getter;

/**
 * Core class of the property system, holds one property.
 * @param <T> The property's type.
 */
public class Property<T> {

    /**
     * Name of the property, as declared in the {@code properties.yaml} file.
     * Must be unique between all properties.
     */
    @Getter
    protected final String name;

    /**
     * Value of the property.
     */
    @Getter
    protected final T value;

    /**
     * Construct a property.
     * @param name Property name, must be unique between all properties.
     * @param value The value of the property.
     */
    public Property(String name, T value) {
        this.name = name;
        this.value = value;
    }
}
