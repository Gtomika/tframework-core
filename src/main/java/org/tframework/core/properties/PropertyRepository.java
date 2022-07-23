package org.tframework.core.properties;

import org.tframework.core.properties.exceptions.NoSuchPropertyException;
import org.tframework.core.properties.exceptions.PropertyException;
import org.tframework.core.test.annotation.NeedsTesting;

/**
 * Stores the properties of the application. {@link PropertyContainer}s can be extracted from it.
 */
@NeedsTesting
public class PropertyRepository {

    //TODO: how to store properties? Map<String, PropertyContainer>?
    //TODO: how to split by flavor?


    public PropertyRepository() {
        //TODO: initialize fields
    }

    public <T> void registerPropertyContainer(PropertyContainer<T> propertyContainer) throws PropertyException {
        //TODO
    }

    public <T> PropertyContainer<T> grabPropertyContainer(String name) throws NoSuchPropertyException {
        //TODO
        return null;
    }
}
