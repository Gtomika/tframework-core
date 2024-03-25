/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.assembler.ElementAssembler;
import org.tframework.core.elements.assembler.ElementAssemblersFactory;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;

/**
 * A wrapper for an element, that keeps track of all the data of this element, and
 * all currently known instances as well.
 */
@Slf4j
@Getter
public abstract class ElementContext {

    protected final String name;
    protected final Class<?> type;
    protected final ElementScope scope;
    protected final ElementSource source;
    protected final ElementAssembler elementAssembler;

    /**
     * Creates a new element context. To activate this context, {@link #initialize()} must also be called.
     * @param name The name of the element. If this is {@link Element#NAME_NOT_SPECIFIED}, then a name
     *             will be assigned based on the type, using {@link ElementUtils#getElementNameByType(Class)}.
     * @param type Type of the element.
     * @param scope {@link ElementScope} of this element.
     * @param source {@link ElementSource} describing where this element was found.
     * @param dependencyResolutionInput Input that enables this context to resolve its own dependencies.
     */
    protected ElementContext(
            @NonNull String name,
            @NonNull Class<?> type,
            @NonNull ElementScope scope,
            ElementSource source,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        this.name = Element.NAME_NOT_SPECIFIED.equals(name) ? ElementUtils.getElementNameByType(type) : name;
        this.type = type;
        this.scope = scope;
        this.source = source;
        this.elementAssembler = initializeElementAssembler(this.name, source, dependencyResolutionInput);
    }

    private ElementAssembler initializeElementAssembler(
            String name,
            ElementSource elementSource,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        if(elementSource != null && dependencyResolutionInput != null) {
            return ElementAssemblersFactory.createElementAssembler(this, dependencyResolutionInput);
        } else {
            log.debug("Will not create assembler for element '{}', because the required " +
                    "input was not provided", name);
            return null;
        }
    }

    /**
     * Initializes this element context so that it is ready to create instances of the element.
     */
    public abstract void initialize();

    /**
     * Requests this element context to return with an instance of the element. The instance will
     * be post-processed and initialized.
     * Depending on the implementation, this might reuse an existing instance, or create a new one.
     */
    public Object requestInstance() {
        return requestInstance(ElementDependencyGraph.empty());
    }

    /**
     * Requests this element context to return with an instance of the element, continuing the
     * dependency resolution process with the given {@link ElementDependencyGraph}. The instance will
     * be post-processed and initialized.
     * @param dependencyGraph The {@link ElementDependencyGraph} to use for dependency resolution.
     */
    public Object requestInstance(ElementDependencyGraph dependencyGraph) {
        var instanceRequest = requestInstanceInternal(dependencyGraph);
        if(!instanceRequest.reused()) {
            postProcessInstance(instanceRequest.instance());
        }
        return instanceRequest.instance();
    }

    /**
     * Request an instance of this element. Depending on the implementation, this may create a new one, or
     * re-use an exiting one. It is not the responsibility of this method to perform and post-processing
     * or initialization on the instance.
     * @param dependencyGraph The {@link ElementDependencyGraph} to use for dependency resolution.
     * @return An {@link InstanceRequest} with details about the returned instance.
     */
    protected abstract InstanceRequest requestInstanceInternal(ElementDependencyGraph dependencyGraph);

    protected void postProcessInstance(Object instance) {
        log.debug("Post processing instance is not yet implemented");
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ElementContext elementContext && elementContext.name.equals(name);
    }

    @Override
    public String toString() {
        return "ElementContext{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", scope=" + scope +
                ", source=" + source +
                '}';
    }

    /**
     * Creates an {@link ElementContext} from the given {@link Element} annotation and additional input.
     * @param elementAnnotation The {@link Element} annotation: determines the scope and name of the element.
     * @param type The type of the element.
     * @param source The {@link ElementSource} of the element.
     * @param dependencyResolutionInput {@link DependencyResolutionInput} that allows the context to resolve
     *                                  its own dependencies.
     */
    public static ElementContext from(
            Element elementAnnotation,
            Class<?> type,
            ElementSource source,
            DependencyResolutionInput dependencyResolutionInput
    ) {
        return switch (elementAnnotation.scope()) {
            case SINGLETON -> new SingletonElementContext(
                    elementAnnotation.name(),
                    type,
                    source,
                    dependencyResolutionInput
            );
            case PROTOTYPE -> new PrototypeElementContext(
                    elementAnnotation.name(),
                    type,
                    source,
                    dependencyResolutionInput
            );
        };
    }

}
