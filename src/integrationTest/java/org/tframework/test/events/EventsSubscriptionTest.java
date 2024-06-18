/* Licensed under Apache-2.0 2024. */
package org.tframework.test.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.tframework.test.commons.utils.TframeworkAssertions.assertHasElement;

import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.events.CoreEvents;
import org.tframework.core.events.EventManager;
import org.tframework.core.events.annotations.Subscribe;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class EventsSubscriptionTest {

    private static final String CUSTOM_TOPIC = "custom-topic";
    private static final String CUSTOM_EVENT_PAYLOAD = "custom-event-payload";

    private Application initEventPayload;
    private String customEventPayload;

    @Subscribe(CoreEvents.APPLICATION_INITIALIZED)
    public void onApplicationInitialized(Application application) {
        initEventPayload = application;
        //when app is initialized, publish a custom event
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
