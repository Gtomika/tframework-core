/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context;

import lombok.EqualsAndHashCode;
import org.tframework.core.di.ElementScope;
import org.tframework.core.di.context.source.ElementSource;

@EqualsAndHashCode(callSuper = true)
public final class SingletonElementContext extends ElementContext {

    public SingletonElementContext(String name, Class<?> type, ElementSource source) {
        super(name, type, ElementScope.SINGLETON, source);
    }

    @Override
    public Object requestInstance() {
        return null;
    }
}
