/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events.publisher;

import org.tframework.core.events.Event;
import org.tframework.core.events.Subscription;

/**
 * The event publisher is invoking a subscribers callback, when an event is published.
 */
public interface EventPublisher {

    /**
     * Publishes an event to a subscriber.
     * @param event The event to be published.
     * @param subscription The subscription to publish the event to.
     */
    void publish(Event event, Subscription subscription);

}
