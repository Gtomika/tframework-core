/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.context;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.di.ElementScope;
import org.tframework.core.di.ElementUtils;
import org.tframework.core.di.annotations.Element;

/**
 * A wrapper for an element, that keeps track of all the data of this element, and
 * all currently known instances as well.
 * @param <T> The type of the element.
 */
@Slf4j
@Getter
@EqualsAndHashCode
public sealed abstract class ElementContext<T> permits SingletonElementContext, PrototypeElementContext {

    protected final String name;
    protected final Class<T> type;
    protected final ElementScope scope;

    /**
     * Creates a new element context.
     * @param name The name of the element. If this is {@link Element#NAME_NOT_SPECIFIED}, it will be
     *             deduced from {@code type}, using {@link ElementUtils#getElementNameByType(Class)}.
     * @param type Type of the element.
     * @param scope {@link ElementScope} of this element.
     */
    protected ElementContext(@NonNull String name, @NonNull Class<T> type, @NonNull ElementScope scope) {
        this.name = name.equals(Element.NAME_NOT_SPECIFIED) ? ElementUtils.getElementNameByType(type) : name;
        this.type = type;
        this.scope = scope;
        log.trace("Element context created. Name: {}, type: {}, scope: {}", name, type.getName(), scope);
    }

    /**
     * Requests this element context to return with an instance of the element.
     * Depending on the implementation, this might reuse an existing instance, or
     * create a new one.
     */
    public abstract T requestInstance();

}
