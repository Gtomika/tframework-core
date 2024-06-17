/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events.publisher;

import org.tframework.core.events.Event;
import org.tframework.core.events.Subscription;

/**
 * This {@link EventPublisher} simply invokes the callback of the subscription and
 * waits until it's completed. It is the most basic implementation of an event publisher. It has the
 * danger that if a callback takes too long to complete, it will block the event management from
 * handling other events.
 */
public class SimpleEventPublisher implements EventPublisher {

    @Override
    public void publish(Event event, Subscription subscription) {
        subscription.callback().accept(event.payload());
    }
}
