package org.tframework.core.elements.context;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.ElementScope;
import org.tframework.core.elements.ElementUtils;
import org.tframework.core.elements.dependency.DependencyResolutionInput;
import org.tframework.core.elements.dependency.graph.DependencyGraph;

/**
 * A special {@link ElementContext} for {@link org.tframework.core.elements.annotations.PreConstructedElement}s.
 * In behaviour, these element contexts are similar to {@link SingletonElementContext}, the only difference is that
 * the instance is already created and does not have to be initialized.
 */
@Slf4j
public class PreConstructedElementContext extends ElementContext {

    private final Object preConstructedInstance;

    private PreConstructedElementContext(Object preConstructedInstance) {
        super(
                ElementUtils.getElementNameByType(preConstructedInstance.getClass()),
                preConstructedInstance.getClass(),
                ElementScope.SINGLETON,
                null //no source for pre-constructed elements
        );
        this.preConstructedInstance = preConstructedInstance;
    }

    @Override
    public void initialize(DependencyResolutionInput input) {
        log.trace("Pre-constructed element '{}' does not need to be initialized, skipping...", name);
    }

    @Override
    public Object requestInstance(DependencyGraph dependencyGraph) {
        return preConstructedInstance;
    }

    /**
     * Creates a new {@link PreConstructedElementContext} for the given pre-constructed instance.
     */
    public static PreConstructedElementContext of(@NonNull Object preConstructedInstance) {
        return new PreConstructedElementContext(preConstructedInstance);
    }
}
