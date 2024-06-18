/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events;

/**
 * Represents an event that can be published to subscribers of a topic.
 * @param topic The topic to which this event should be published.
 * @param payload The payload of the event.
 * @see EventManager
 */
public record Event(
        String topic,
        Object payload
) {
}
