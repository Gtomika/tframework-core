/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.assembler.ElementAssembler;
import org.tframework.core.elements.assembler.ElementAssemblersFactory;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.dependency.DependencyResolutionInput;
import org.tframework.core.elements.dependency.DependencyResolver;

/**
 * A wrapper for an element, that keeps track of all the data of this element, and
 * all currently known instances as well.
 */
@Slf4j
@Getter
@ToString
@EqualsAndHashCode
public sealed abstract class ElementContext permits SingletonElementContext, PrototypeElementContext {

    protected final String name;
    protected final Class<?> type;
    protected final ElementScope scope;
    protected final ElementSource source;
    protected ElementAssembler elementAssembler; //initialized later

    /**
     * Creates a new element context. To activate this context, {@link #initialize(DependencyResolutionInput)} must
     * also be called.
     * @param name The name of the element. If this is {@link Element#NAME_NOT_SPECIFIED}, it will be
     *             deduced from {@code type}, using {@link ElementUtils#getElementNameByType(Class)}.
     * @param type Type of the element.
     * @param scope {@link ElementScope} of this element.
     */
    protected ElementContext(
            @NonNull String name,
            @NonNull Class<?> type,
            @NonNull ElementScope scope,
            @NonNull ElementSource source
    ) {
        this.name = name.equals(Element.NAME_NOT_SPECIFIED) ? ElementUtils.getElementNameByType(type) : name;
        this.type = type;
        this.scope = scope;
        this.source = source;
    }

    /**
     * Initializes this element context so that it is ready to create instances of the element. Subclasses may override
     * this method to perform additional initialization steps, but they must call the superclass implementation first.
     * @param input The {@link DependencyResolutionInput} to use to create appropriate {@link DependencyResolver}s.
     */
    public void initialize(DependencyResolutionInput input) {
        elementAssembler = ElementAssemblersFactory.createElementAssembler(name, type, source, input);
        log.trace("Initialized element context: {}. It will use assembler: {}", name, elementAssembler.getClass().getName());
    }

    /**
     * Requests this element context to return with an instance of the element.
     * Depending on the implementation, this might reuse an existing instance, or create a new one.
     */
    public abstract Object requestInstance();

    /**
     * Creates an {@link ElementContext} from the given {@link Element} annotation and additional required input.
     * @param elementAnnotation The {@link Element} annotation: determines the scope and name of the element.
     * @param type The type of the element.
     * @param source The {@link ElementSource} of the element.
     */
    public static ElementContext from(
            Element elementAnnotation,
            Class<?> type,
            ElementSource source
    ) {
        return switch (elementAnnotation.scope()) {
            case SINGLETON -> new SingletonElementContext(
                    elementAnnotation.name(),
                    type,
                    source
            );
            case PROTOTYPE -> new PrototypeElementContext(
                    elementAnnotation.name(),
                    type,
                    source
            );
        };
    }

}
