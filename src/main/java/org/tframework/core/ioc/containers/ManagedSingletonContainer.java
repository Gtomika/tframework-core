/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.containers;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.tframework.core.ioc.ManagingType;

/**
 * Wraps a managed singleton class and provides additional details and data about it.
 *
 * @param <T> The type of the managed class.
 */
public class ManagedSingletonContainer<T> extends AbstractContainer<T> {

    private final T instance;

    public ManagedSingletonContainer(String name, Class<T> managedEntity, T instance) {
        super(ManagingType.SINGLETON, name, managedEntity);
        this.instance = instance;
    }

    public T grabInstance() {
        return instance;
    }
}
