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
package org.tframework.test.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.tframework.test.commons.utils.TframeworkAssertions.assertHasElement;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.events.CoreEvents;
import org.tframework.core.events.EventManager;
import org.tframework.core.events.annotations.Subscribe;
import org.tframework.test.commons.annotations.EventDelay;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@Slf4j
@EventDelay(topic = EventsSubscriptionTest.CUSTOM_TOPIC)
@IsolatedTFrameworkTest
public class EventsSubscriptionTest {

    public static final String CUSTOM_TOPIC = "custom-topic";
    private static final String CUSTOM_EVENT_PAYLOAD = "custom-event-payload";

    private Application initEventPayload;
    private String customEventPayload;

    @Subscribe(CoreEvents.APPLICATION_INITIALIZED)
    public void onApplicationInitialized(Application application) {
        initEventPayload = application;
        //when app is initialized, publish a custom event (with delay)
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            log.error("Error while sleeping", e);
        }
        application.getElementsContainer().getElement(EventManager.class)
                .publish(CUSTOM_TOPIC, CUSTOM_EVENT_PAYLOAD);
    }

    @Subscribe(CUSTOM_TOPIC)
    public void onCustomEvent(String payload) {
        customEventPayload = payload;
    }

    @Test
    public void shouldHaveEventsManagerElement(@InjectElement ElementsContainer container) {
        assertHasElement(container, EventManager.class);
    }

    @Test
    public void shouldHaveReceivedApplicationInitializedEvent(@InjectElement Application application) {
        assertEquals(application, initEventPayload);
    }

    @Test
    public void shouldHaveReceivedCustomEvent() {
        assertEquals(CUSTOM_EVENT_PAYLOAD, customEventPayload);
    }
}
