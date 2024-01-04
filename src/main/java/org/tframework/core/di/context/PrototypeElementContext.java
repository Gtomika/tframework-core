/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context;

import lombok.EqualsAndHashCode;
import org.tframework.core.di.ElementScope;

@EqualsAndHashCode(callSuper = true)
public final class PrototypeElementContext extends ElementContext {

    public PrototypeElementContext(String name, Class<?> type) {
        super(name, type, ElementScope.PROTOTYPE);
    }

    @Override
    public Object requestInstance() {
        return null;
    }
}
