/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.filter;

import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.context.ElementContext;

/**
 * The element context filter decides if an {@link ElementContext} should be kept
 * and added to the {@link ElementsContainer}, or discarded.
 */
public interface ElementContextFilter {

    /**
     * Performs the filtering.
     * @param elementContext {@link ElementContext} to filter.
     * @return True if the context should be discarded, false if it should be kept.
     */
    boolean discardElementContext(ElementContext elementContext);

}
