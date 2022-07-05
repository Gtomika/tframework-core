package org.tframework.core.ioc.containers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.tframework.core.ioc.ManagingType;
import org.tframework.core.ioc.exceptions.NotConstructibleException;

/**
 * Base class for containers that wrap managed entities.
 */
@RequiredArgsConstructor
public abstract class AbstractContainer<T> {

    /**
     * Managing type of the entity wrapped by this container.
     */
    @Getter
    protected final ManagingType managingType;

    /**
     * Name of the entity wrapped by this container. Unique across all entities.
     */
    @Getter
    protected final String name;

    /**
     * Actual instance of the managed entity.
     */
    @Getter
    protected final Class<T> instanceType;

    /**
     * Gets an instance of this managed entity. The container has control of creating a
     * new instance, or using an already existing one.
     * @throws NotConstructibleException If no instance can be constructed and provided.
     */
    public abstract T grabInstance() throws NotConstructibleException;

}
