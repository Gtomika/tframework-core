/* Licensed under Apache-2.0 2023. */
package org.tframework.core.loop;

import java.util.function.Consumer;

public record Subscription(
        String subscriptionName,
        String eventName,
        Consumer<Event> callback
) {

    public Subscription {
        if(subscriptionName == null) {
            throw new IllegalArgumentException("Subscriptions name cannot be null.");
        }
        if(eventName == null) {
            throw new IllegalArgumentException("Subscriptions event name cannot be null.");
        }
        if(callback == null) {
            throw new IllegalArgumentException("Subscriptions callback cannot be null.");
        }
    }

}
