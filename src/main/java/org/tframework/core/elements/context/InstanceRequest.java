/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context;

import org.tframework.core.TFrameworkInternal;

/**
 * Internal data holder used when element instances are requested.
 * @param instance The requested instance of the element.
 * @param reused True if the instance was not newly created, but reused. For example,
 *               a singleton element context will reuse the existing instance.
 */
@TFrameworkInternal
public record InstanceRequest(
        Object instance,
        boolean reused
) {

    public static InstanceRequest ofReused(Object instance) {
        return new InstanceRequest(instance, true);
    }

    public static InstanceRequest ofNewlyCreated(Object instance) {
        return new InstanceRequest(instance, false);
    }

}
