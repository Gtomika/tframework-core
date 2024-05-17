/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events;

import org.tframework.core.elements.annotations.Element;
import org.tframework.core.events.publisher.EventPublisherFactory;

/**
 * Configurations related to the framework's event system.
 */
@Element
public class EventConfig {

    /**
     * Provides the {@link EventManager} element for the application.
     */
    @Element
    public EventManager registerEventManager() {
        var eventPublisher = EventPublisherFactory.createDefaultEventPublisher();
        return new EventManager(eventPublisher);
    }

}
