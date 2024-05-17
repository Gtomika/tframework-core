/* Licensed under Apache-2.0 2024. */
package org.tframework.test.events;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.events.EventManager;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class EventsManagerElementTest {

    @Test
    public void shouldHaveEventsManagerElement(@InjectElement EventManager eventManager) {
        assertNotNull(eventManager);
    }

}
