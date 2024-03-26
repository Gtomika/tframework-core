/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.postprocessing;

import org.tframework.core.elements.context.ElementContext;

/**
 * Performs post-processing of a newly created element instance. This may include modifications
 * to the instance, or simply invoking certain methods of it.
 */
public interface ElementInstancePostProcessor {

    /**
     * Performs the post-processing.
     * @param elementContext The {@link ElementContext} to which the instance belongs.
     * @param instance The newly created instance. It may be modified during this process.
     */
    void postProcessInstance(ElementContext elementContext, Object instance);

}
