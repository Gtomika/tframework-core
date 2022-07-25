package org.tframework.core.properties;

import org.tframework.core.ioc.constants.ManagingType;
import org.tframework.core.ioc.containers.AbstractContainer;
import org.tframework.core.ioc.exceptions.NotConstructibleException;
import org.tframework.core.properties.exceptions.PropertyException;

/**
 * A container that hold a property. It exists to integrate properties into the IoC flow and
 * allow them to be injected.
 * @param <T> The type of the property.
 */
public class PropertyContainer<T> extends AbstractContainer<T> {

    /**
     * The property held by this container.
     */
    private Property<T> property;

    /**
     * Builds a property container from a property.
     */
    public PropertyContainer(Property<T> property) {
        super(ManagingType.PROPERTY, property.getName(), (Class<T>) property.getValue().getClass());
    }

    /**
     * Gets the value of the property.
     * @throws NotConstructibleException If the value cannot be returned.
     */
    @Override
    public T grabInstance() throws NotConstructibleException, PropertyException {
        try {
            return property.getValue();
        } catch (Exception e) {
            throw new PropertyException(name, e);
        }
    }

    /**
     * @return The name of the containing property.
     */
    public String getPropertyName() {
        return property.getName();
    }
}
