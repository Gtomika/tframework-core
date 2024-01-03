/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context;

import lombok.EqualsAndHashCode;
import org.tframework.core.di.ElementScope;

@EqualsAndHashCode(callSuper = true)
public final class SingletonElementContext<T> extends ElementContext<T> {

    public SingletonElementContext(String name, Class<T> type) {
        super(name, type, ElementScope.SINGLETON);
    }

    @Override
    public T requestInstance() {
        return null;
    }
}
