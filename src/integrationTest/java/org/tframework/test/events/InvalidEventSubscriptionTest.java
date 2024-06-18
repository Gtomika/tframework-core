/* Licensed under Apache-2.0 2024. */
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
