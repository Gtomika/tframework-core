/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events.publisher;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Factory for creating {@link EventPublisher}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventPublisherFactory {

    /**
     * Creates a default {@link EventPublisher} for the framework to use.
     */
    public static EventPublisher createDefaultEventPublisher() {
        return createAsyncVirtualEventPublisher();
    }

    public static SimpleEventPublisher createSimpleEventPublisher() {
        return new SimpleEventPublisher();
    }

    public static AsyncVirtualEventPublisher createAsyncVirtualEventPublisher() {
        return new AsyncVirtualEventPublisher();
    }

}
