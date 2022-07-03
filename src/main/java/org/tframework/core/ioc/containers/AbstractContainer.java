package org.tframework.core.ioc.containers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.tframework.core.ioc.ManagingType;

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

}
