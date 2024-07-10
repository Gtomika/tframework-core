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
