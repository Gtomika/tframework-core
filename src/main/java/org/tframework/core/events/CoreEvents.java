/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.Application;

/**
 * Defines topics for events produced by the TFramework core module.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CoreEvents {

    /**
     * Event topic for the application initialization event. The payload of this event
     * will be the {@link Application} instance.
     */
    public static final String APPLICATION_INITIALIZED = "org-tframework-core-application-initialized";

}
