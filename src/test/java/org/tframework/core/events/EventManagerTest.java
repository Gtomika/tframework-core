/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class EventManagerTest {

    public static final String EVENT_NAME = "testEvent";

    private EventManager eventManager;

    private boolean eventHandled;

    @BeforeEach
    public void setUp() {
        eventManager = new EventManager();
        eventHandled = false;
    }

    @Test
    public void shouldNotPublishEvent_whenNoSubscribersExist() {
        eventManager.publish(EVENT_NAME, "payload");
        assertFalse(eventHandled);
    }

    @Test
    public void shouldNotPublishEvent_whenSubscriberThrowsException() {
        eventManager.subscribe(EVENT_NAME, this::invalidCallback);
        eventManager.publish(EVENT_NAME, "payload");
        assertFalse(eventHandled);
    }

    @Test
    public void shouldPublishEvent_whenSubscriberExists() {
        eventManager.subscribe(EVENT_NAME, this::handleEvent);
        eventManager.publish(EVENT_NAME, "payload");
        assertTrue(eventHandled);
    }

    @Test
    public void shouldPublishEvent_whenMultipleSubscribersExist_andOneIsInvalid() {
        eventManager.subscribe(EVENT_NAME, this::invalidCallback);
        eventManager.subscribe(EVENT_NAME, this::handleEvent);
        eventManager.publish(EVENT_NAME, "payload");
        assertTrue(eventHandled);
    }

    @Test
    public void shouldUnsubscribe_whenSubscriptionIdIsProvided() {
        var subscriptionId = eventManager.subscribe(EVENT_NAME, this::handleEvent);
        eventManager.unsubscribe(subscriptionId);
        eventManager.publish(EVENT_NAME, "payload");
        assertFalse(eventHandled);
    }

    private void handleEvent(Object payload) {
        log.debug("Handling event with payload '{}'", payload);
        eventHandled = true;
    }

    private void invalidCallback(Object payload) {
        throw new RuntimeException("I can't handle this");
    }

}
