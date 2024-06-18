/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Represents a subscription to a topic.
 * @param topic The topic that this subscription is for.
 * @param subscriptionId The ID of this subscription, used for further operations, such as unsubscribing.
 * @param callback The callback to be invoked when an event is published to the topic.
 * @see EventManager
 */
public record Subscription(
        String topic,
        UUID subscriptionId,
        Consumer<Object> callback
) {
}
