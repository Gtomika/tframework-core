/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.PreConstructedElement;
import org.tframework.core.events.publisher.EventPublisher;
import org.tframework.core.utils.MultiValueMap;

/**
 * Manages {@link Subscription}s and publishes {@link Event}s to subscribers of topics. This is a pre-constructed
 * element in all TFramework applications. It is the recommended way to publish events. As for subscriptions,
 * use the annotation based approach: TODO
 */
@Slf4j
@PreConstructedElement
public class EventManager {

    private final MultiValueMap<String, Subscription> subscriptions;
    private final EventPublisher eventPublisher;

    EventManager(EventPublisher eventPublisher) {
        subscriptions = new MultiValueMap<>();
        this.eventPublisher = eventPublisher;
    }

    /**
     * Publishes an {@link Event} to all of its topics subscribers, if any exists. If
     * any of the subscribers throws an exception, it is caught and logged, but does not
     * prevent the other subscriptions to be invoked.
     * @param event The event to be published.
     */
    public void publish(Event event) {
        var subscriptionsOfTopic = subscriptions.getOrEmptyList(event.topic());
        if(subscriptionsOfTopic.isEmpty()) {
            log.warn("No subscribers for topic '{}'", event.topic());
            return;
        }
        publishEventToSubscribers(subscriptionsOfTopic, event);
    }

    /**
     * Publishes an event with to the given topic and payload to all of its topics subscribers. This is a
     * convenience method for {@link #publish(Event)}.
     */
    public void publish(String topic, Object payload) {
        publish(new Event(topic, payload));
    }

    /**
     * Subscribes to a topic.
     * @param topic The topic to subscribe to.
     * @param callback The callback to be called when an {@link Event} is published intended for this subscriber.
     * @return The subscription ID, for further operations, such as unsubscribing.
     */
    public synchronized UUID subscribe(String topic, Consumer<Object> callback) {
        var subscription = new Subscription(topic, UUID.randomUUID(), callback);
        subscriptions.putValue(topic, subscription);
        log.debug("Subscribed to topic '{}' with subscription ID '{}'", topic, subscription.subscriptionId());
        return subscription.subscriptionId();
    }

    /**
     * Removes the subscription with the given subscription ID, if it exists.
     * @param subscriptionId The subscription ID that was returned when subscribing.
     */
    public synchronized void unsubscribe(UUID subscriptionId) {
        subscriptions.values().stream()
                .flatMap(List::stream)
                .filter(s -> s.subscriptionId().equals(subscriptionId))
                .findAny()
                .ifPresentOrElse(
                        s -> {
                            subscriptions.removeValue(s.topic(), s);
                            log.debug("Unsubscribed from topic '{}' (subscription ID was '{}')", s.topic(), s.subscriptionId());
                        },
                        () -> log.warn("No subscription found for subscription ID '{}'", subscriptionId)
                );
    }

    private void publishEventToSubscribers(List<Subscription> subscriptions, Event event) {
        subscriptions.forEach(subscription -> {
            try {
                eventPublisher.publish(event, subscription);
                log.trace("Published event to topic '{}' with payload '{}' to subscriber '{}'",
                        event.topic(), event.payload(), subscription.subscriptionId());
            } catch (Exception e) {
                log.error("Failed to publish event to topic '{}' with payload '{}' to subscriber '{}' due to an exception thrown from the callback",
                        event.topic(), event.payload(), subscription.subscriptionId(), e);
            }
        });
    }
}
