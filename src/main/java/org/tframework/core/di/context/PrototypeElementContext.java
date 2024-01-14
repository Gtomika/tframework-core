/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context;

import lombok.EqualsAndHashCode;
import org.tframework.core.di.ElementScope;
import org.tframework.core.di.context.source.ElementSource;

/**
 * An {@link ElementContext} that represents a prototype element.
 * @see ElementScope#PROTOTYPE
 */
@EqualsAndHashCode(callSuper = true)
public final class PrototypeElementContext extends ElementContext {

    public PrototypeElementContext(String name, Class<?> type, ElementSource source) {
        super(name, type, ElementScope.PROTOTYPE, source);
    }

    @Override
    public Object requestInstance() {
        return null;
    }
}
