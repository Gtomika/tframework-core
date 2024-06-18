/* Licensed under Apache-2.0 2024. */
package org.tframework.test.events;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.events.EventManager;
import org.tframework.core.events.annotations.Subscribe;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class FailingEventPublishTest {

    private static final String TEST_TOPIC = "test-topic";

    @Subscribe(TEST_TOPIC)
    public void onTestEvent(String payload) {
        throw new RuntimeException("I can't handle this!");
    }

    @Test
    public void shouldSurviveFailEventPublish(@InjectElement EventManager eventManager) {
        //it just logs the error and continues
        assertDoesNotThrow(() -> eventManager.publish(TEST_TOPIC, "test-payload"));
    }

    @Test
    public void shouldSurviveInvalidPayload(@InjectElement EventManager eventManager) {
        //it just logs the error and continues
        assertDoesNotThrow(() -> eventManager.publish(TEST_TOPIC, new Object()));
    }

}
