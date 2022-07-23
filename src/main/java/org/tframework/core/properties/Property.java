package org.tframework.core.properties;

import lombok.Getter;

/**
 * Core class of the property system, hold one property.
 * @param <T> The property's type.
 */
public class Property<T> {

    /**
     * Name of the property, as declared in the {@code properties.yaml} file.
     */
    @Getter
    protected final String name;

    /**
     * Value of the property.
     */
    @Getter
    protected final T value;

    public Property(String name, T value) {
        this.name = name;
        this.value = value;
    }
}
