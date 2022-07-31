package org.tframework.core.properties.containers;

import lombok.Getter;
import org.tframework.core.ApplicationContext;
import org.tframework.core.ioc.constants.ManagingType;
import org.tframework.core.ioc.containers.AbstractContainer;
import org.tframework.core.ioc.exceptions.NotConstructibleException;
import org.tframework.core.properties.PropertyRepository;
import org.tframework.core.properties.annotations.InjectProperty;
import org.tframework.core.properties.exceptions.NoSuchPropertyException;

/**
 * A container that hold a reference to a property. This class connects the IoC with the
 * property system, allowing properties to be injected as dependencies using the {@link InjectProperty}
 * annotation.
 * <p>
 * Does not store the actual value of the property, only forwards calls to the {@link PropertyRepository}.
 */
public class PropertyContainer extends AbstractContainer<Object> {

    /**
     * Reference to the singleton instance of the property repository.
     */
    private final PropertyRepository propertyRepository;

    /**
     * Name of the referenced property.
     */
    @Getter
    private final String propertyName;

    /**
     * Builds a property container from a property name.
     * @throws NoSuchPropertyException If no property with the name could be found.
     */
    public PropertyContainer(String propertyName) throws NoSuchPropertyException {
        super(
                ManagingType.PROPERTY,
                propertyName,
                //will throw exception if it cannot find this property
                ApplicationContext.getInstance().getPropertyRepository().grabProperty(propertyName).getPropertyType()
        );
        this.propertyRepository = ApplicationContext.getInstance().getPropertyRepository();
        this.propertyName = propertyName;
    }

    /**
     * Gets the value of the property.
     * @throws NotConstructibleException If the property with the name does not exists. This exception is not expected
     * to be thrown here, because the existence is checked at construction time.
     */
    @Override
    public Object grabInstance() throws NotConstructibleException {
        try {
            return propertyRepository.grabProperty(propertyName).getValue();
        } catch (Exception e) {
            throw NotConstructibleException.customNotConstructibleException(
                    String.format("Cannot get value of property '%s'", propertyName), e
            );
        }
    }
}
