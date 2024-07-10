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

import static org.tframework.test.commons.utils.TframeworkAssertions.assertInitializationExceptionWithCause;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.tframework.core.events.annotations.Subscribe;
import org.tframework.core.events.exception.EventSubscriptionException;
import org.tframework.test.commons.annotations.ExpectInitializationFailure;
import org.tframework.test.commons.annotations.InjectInitializationException;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@Slf4j
@IsolatedTFrameworkTest
@ExpectInitializationFailure
public class InvalidEventSubscriptionTest {

    @Subscribe("test-topic")
    public void onTestEvent() { //invalid, because it has no parameter for payload
        log.info("Received test event");
    }

    @Test
    public void shouldFailInitialization(@InjectInitializationException Exception e) {
        assertInitializationExceptionWithCause(e, EventSubscriptionException.class);
    }

}
