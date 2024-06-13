/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.postprocessing;

import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.Priority;
import org.tframework.core.elements.context.ElementContext;

/**
 * Performs post-processing of a newly created element instance. This may include modifications
 * to the instance, or simply invoking certain methods of it. Notes:
 * <ul>
 *     <li>
 *         You can define your own custom post processors as {@link Element}s.
 *         These will be applied, as well as the built-in ones.
 *     </li>
 *     <li>
 *         You can use {@link Priority} to specify the order of execution of multiple post processors.
 *         It is recommended not to use very high or low values, as they may match with built-in processors
 *         and cause unexpected behavior.
 *     </li>
 * </ul>
 */
public interface ElementInstancePostProcessor {

    /**
     * Performs the post-processing.
     * @param elementContext The {@link ElementContext} to which the instance belongs.
     * @param instance The newly created instance. It may be modified during this process.
     */
    void postProcessInstance(ElementContext elementContext, Object instance);

}
