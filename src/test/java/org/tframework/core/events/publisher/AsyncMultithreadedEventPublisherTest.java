/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events.publisher;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tframework.core.events.Event;
import org.tframework.core.events.Subscription;

public class AsyncMultithreadedEventPublisherTest {

    private final AsyncMultithreadedEventPublisher publisher = new AsyncMultithreadedEventPublisher(1);

    private boolean published = false;

    @BeforeEach
    public void setUp() {
        published = false;
    }

    @Test
    public void shouldPublishEvent() {
        var subscription = new Subscription("topic", UUID.randomUUID(), this::callback);
        var event = new Event("topic", "payload");
        publisher.publish(event, subscription);
        publisher.shutdown(1000);
        assertTrue(published);
    }

    private void callback(Object payload) {
        published = true;
    }

}
