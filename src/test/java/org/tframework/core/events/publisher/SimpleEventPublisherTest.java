/*
Copyright 2024 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core.events.publisher;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tframework.core.events.Event;
import org.tframework.core.events.Subscription;

public class SimpleEventPublisherTest {

    private final SimpleEventPublisher publisher = new SimpleEventPublisher();

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
        assertTrue(published);
    }

    private void callback(Object payload) {
        published = true;
    }

}
