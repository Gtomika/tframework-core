/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context;

import lombok.EqualsAndHashCode;
import org.tframework.core.di.ElementScope;

@EqualsAndHashCode(callSuper = true)
public final class PrototypeElementContext<T> extends ElementContext<T> {

    public PrototypeElementContext(String name, Class<T> type) {
        super(name, type, ElementScope.PROTOTYPE);
    }

    @Override
    public T requestInstance() {
        return null;
    }
}
