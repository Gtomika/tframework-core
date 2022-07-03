/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Wraps a managed class and provides additional details and data about it.
 *
 * @param <T> The type of the managed class.
 */
@Builder
@RequiredArgsConstructor
public class ManagedContainer<T> {

    /** Type of the managed entity. */
    @Getter
    @NonNull
    private final ManagedType managedType;

    /** Name of the managed entity. This is unique across all managed entities. */
    @Getter
    @NonNull
    private final String name;

    /** Class of the managed entity. */
    @Getter
    @NonNull
    private final Class<T> instanceType;

    /** Actual instance of the managed entity. */
    @NonNull
    private final T instance;

    public T grab() {
        return instance;
    }
}
