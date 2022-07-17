package org.tframework.core.ioc.containers;

import lombok.Getter;
import org.tframework.core.ioc.constants.ManagingType;
import org.tframework.core.ioc.exceptions.NotConstructibleException;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Wraps a multi instance managed entity. Contains all the instances of this entity
 * and additional data about them.
 * @param <T> Type of the multi instance managed entity.
 */
public class ManagedMultiInstanceContainer<T> extends AbstractContainer<T> {

    @Getter
    private final List<T> instances;

    /**
     * Construct a new container for a multi instance entity. It will contain
     * no instances.
     * @param name Name of the entity.
     * @param instanceType Class of the entity.
     */
    public ManagedMultiInstanceContainer(String name, Class<T> instanceType) {
        super(ManagingType.MULTI_INSTANCE, name, instanceType);
        this.instances = new ArrayList<>();
    }

    /**
     * Constructor where it is possible to specify a provider method.
     * @param name Name of the entity.
     * @param instanceType The class of the managed entity.
     * @param providerMethod Method that can be called to construct an instance. Can be null if this is not a provided entity.
     * @throws IllegalArgumentException If the 'providerMethod' was not null, and it was invalid.
     */
    public ManagedMultiInstanceContainer(
            String name,
            Class<T> instanceType,
            @Nullable Method providerMethod
    ) throws IllegalArgumentException {
        super(ManagingType.MULTI_INSTANCE, name, instanceType, providerMethod);
        this.instances = new ArrayList<>();
    }

    /**
     * Returns count of how many instances are currently present
     * for this managed entity.
     */
    public int currentInstanceCount() {
        return instances.size();
    }

    /**
     * Constructs a new instance of this managed entity.
     * @return The new instance.
     * @throws NotConstructibleException If no new instance could be created.
     */
    private T constructNewInstance() throws NotConstructibleException {
        T instance = null; //TODO: use managed entity constructor
        instances.add(instance);
        return instance;
    }

    /**
     * Constructs and returns a new instance of the managed multi-instance entity.
     * @throws NotConstructibleException If no new instance could be created.
     */
    @Override
    public T grabInstance() throws NotConstructibleException {
        return constructNewInstance();
    }
}
