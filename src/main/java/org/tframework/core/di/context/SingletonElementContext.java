/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context;

import lombok.EqualsAndHashCode;
import org.tframework.core.di.ElementScope;

@EqualsAndHashCode(callSuper = true)
public final class SingletonElementContext extends ElementContext {

    public SingletonElementContext(String name, Class<?> type) {
        super(name, type, ElementScope.SINGLETON);
    }

    @Override
    public Object requestInstance() {
        return null;
    }
}
