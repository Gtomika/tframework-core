/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.filter;

import org.tframework.core.Application;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.ElementContext;

/**
 * The element context filter decides if an {@link ElementContext} should be kept
 * and added to the {@link ElementsContainer}, or discarded. Notes:
 * <ul>
 *     <li>
 *         You can define you own filters by implementing this interface and
 *         marking your class as {@link Element}.
 *     </li>
 *     <li>
 *         If you define your own filters as elements, and those have their own
 *         dependency elements, those will be resolved and injected by the framework, as expected.
 *         But because this happens <b>BEFORE</b> the filtering is applied, you should be careful
 *         not to "pull in" elements that are otherwise would be filtered out. An absurd example
 *         is when you define a filter to remove element {@code A}, and you add {@code A} to it
 *         as a dependency. In this case, {@code A} will not be filtered.
 *     </li>
 * </ul>
 */
public interface ElementContextFilter {

    /**
     * Performs the filtering.
     * @param elementContext {@link ElementContext} to check if it should be filtered.
     * @param application {@link Application} with many details that can be used to build
     *                                       filtering logic. For example, we can get the profiles,
     *                                       or properties or even other elements from here.
     * @return True if the context should be discarded, false if it should be kept.
     */
    boolean discardElementContext(ElementContext elementContext, Application application);

}
