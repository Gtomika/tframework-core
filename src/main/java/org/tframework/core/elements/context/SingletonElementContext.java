/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context;

import lombok.EqualsAndHashCode;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.dependency.DependencyResolutionInput;

/**
 * An {@link ElementContext} that represents a singleton element.
 * @see ElementScope#SINGLETON
 */
@EqualsAndHashCode(callSuper = true)
public final class SingletonElementContext extends ElementContext {

    public SingletonElementContext(
            String name,
            Class<?> type,
            ElementSource source
    ) {
        super(name, type, ElementScope.SINGLETON, source);
    }

    @Override
    public void initialize(DependencyResolutionInput input) {
        super.initialize(input);
        //TODO: initialize the only instance
    }

    @Override
    public Object requestInstance() {
        return null; //TODO: use assembler to create instance, only ONCE!
    }
}
