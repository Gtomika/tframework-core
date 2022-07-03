package org.tframework.core.ioc.containers;

import lombok.Getter;
import org.tframework.core.ioc.ManagingType;

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
     * Returns count of how many instances are currently present
     * for this managed entity.
     */
    public int currentInstanceCount() {
        return instances.size();
    }

    /**
     * Constructs a new instance of this managed entity.
     * @return The new instance.
     */
    public T constructNewInstance() {
        T instance = null; //TODO: use managed entity constructor
        instances.add(instance);
        return instance;
    }
}
