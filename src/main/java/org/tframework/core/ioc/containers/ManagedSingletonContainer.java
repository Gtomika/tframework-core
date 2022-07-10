/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.containers;

import org.tframework.core.ioc.ManagedEntityConstructor;
import org.tframework.core.ioc.constants.ManagingType;
import org.tframework.core.ioc.exceptions.NotConstructibleException;

/**
 * Wraps a managed singleton class and provides additional details and data about it.
 *
 * @param <T> The type of the managed class.
 */
public class ManagedSingletonContainer<T> extends AbstractContainer<T> {

    private T instance;

    /**
     * Create a container that holds a managed singleton entity. The actual instance of not yet initialized.
     * @param name Name of the entity.
     * @param managedEntityClass The class of the managed entity.
     */
    public ManagedSingletonContainer(String name, Class<T> managedEntityClass) {
        super(ManagingType.SINGLETON, name, managedEntityClass);
        //this.instance remains null
    }

    /**
     * Create a container with a predefined instance. Usually this should not be used, because the container should
     * create its own instances, but there are times when it is necessary, such as when creating container of
     * {@link org.tframework.core.ApplicationContext}. In this case, the managed entity cannot have dependencies!
     * @param name Name of the entity.
     * @param managedEntity The class of the managed entity.
     * @param instance The singleton instance.
     */
    public ManagedSingletonContainer(String name, Class<T> managedEntity, T instance) {
        super(ManagingType.SINGLETON, name, managedEntity);
        this.instance = instance;
    }

    /**
     * Gets the only, singleton instance of the managed entity. If this is not constructed,
     * it will be now.
     * @throws NotConstructibleException If the singleton instance could not be constructed.
     */
    @Override
    public T grabInstance() throws NotConstructibleException {
        if(instance == null) {
            ManagedEntityConstructor.constructManagedEntity(instanceType);
        }
        return instance;
    }
}
