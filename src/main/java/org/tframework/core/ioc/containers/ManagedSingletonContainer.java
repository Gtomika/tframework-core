/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.containers;

import org.tframework.core.annotations.TFrameworkInternal;
import org.tframework.core.ioc.DependencyResolver;
import org.tframework.core.ioc.constants.ManagingType;
import org.tframework.core.ioc.exceptions.NotConstructibleException;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Objects;

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
    }

    /**
     * Create a container with a predefined instance. Usually this should not be used, because the container should
     * create its own instances, but there are times when it is necessary, such as when creating container of
     * {@link org.tframework.core.ApplicationContext}. In this case, the managed entity cannot have dependencies!
     * @param name Name of the entity.
     * @param managedEntityClass The class of the managed entity.
     * @param instance The singleton instance.
     */
    @TFrameworkInternal
    public ManagedSingletonContainer(String name, Class<T> managedEntityClass, T instance) {
        super(ManagingType.SINGLETON, name, managedEntityClass, true);
        this.instance = instance;
    }

    /**
     * Constructor where it is possible to specify a provider method.
     * @param name Name of the entity.
     * @param managedEntityClass The class of the managed entity.
     * @param providerMethod Method that can be called to construct an instance. Can be null if this is not a provided entity.
     * @throws IllegalArgumentException If the 'providerMethod' was not null, and it was invalid.
     */
    public ManagedSingletonContainer(
            String name,
            Class<T> managedEntityClass,
            @Nullable Method providerMethod
    ) throws IllegalArgumentException {
        super(ManagingType.SINGLETON, name, managedEntityClass, providerMethod);
    }

    /**
     * Gets the only, singleton instance of the managed entity. If this is not constructed,
     * it will be now.
     * @throws NotConstructibleException If the singleton instance could not be constructed.
     */
    @Override
    public T grabInstance() throws NotConstructibleException {
        if(instance == null) {
            Objects.requireNonNull(managedEntityConstructor);
            instance = managedEntityConstructor.constructManagedEntity(dependencyInformationList);
            DependencyResolver.resolveAfterConstructDependencies(instance, name, dependencyInformationList);
        }
        return instance;
    }
}
