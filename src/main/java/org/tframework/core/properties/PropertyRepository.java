package org.tframework.core.properties;

import org.tframework.core.annotations.NeedsTesting;
import org.tframework.core.properties.exceptions.NoSuchPropertyException;
import org.tframework.core.properties.exceptions.PropertyRedefinitionException;

/**
 * Stores the properties of the application. {@link Property}-s can be extracted from it.
 */
@NeedsTesting
public class PropertyRepository {

    //TODO: how to store properties? Map<String, PropertyContainer>?
    //TODO: how to split by flavor?


    public PropertyRepository() {
        //TODO: initialize fields
    }

    public <T> void registerProperty(Property<T> property) throws PropertyRedefinitionException {
        //TODO
    }

    public <T> Property<T> grabProperty(String name) throws NoSuchPropertyException {
        //TODO
        return null;
    }
}
