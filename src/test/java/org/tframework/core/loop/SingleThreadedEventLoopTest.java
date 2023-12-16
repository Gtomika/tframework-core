/* Licensed under Apache-2.0 2023. */
package org.tframework.core.loop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Slf4j
class SingleThreadedEventLoopTest {

    private static final int WAIT_TIME_MS = 50;

    private SingleThreadedEventLoop loop;
    private Dispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        dispatcher = new AsynchronousDispatcher();
        loop = new SingleThreadedEventLoop("test-loop", dispatcher);
    }

    @Test
    public void shouldSubscribe_andDispatchEvent() throws Exception {
        String testEventName = "test-event";
        loop.start();

        Subscription subscription = new Subscription("test-subscription", testEventName, event -> {
            log.info("I'm the test event subscription");
        });
        loop.subscribe(subscription);

        Event event = Event.create(testEventName);
        loop.submit(event);

        loop.stop(false);
        Thread.sleep(WAIT_TIME_MS);

        assertEquals(1, loop.getProcessedEventsAmount().get());
    }

    @Test
    public void shouldNotDispatchEvent_whenNoSubscriptions() throws Exception {
        loop.start();

        Event event = Event.create("test-event");
        loop.submit(event);

        loop.stop(false);
        Thread.sleep(WAIT_TIME_MS);

        assertEquals(1, loop.getProcessedEventsAmount().get());
    }

    @Test
    public void shouldNotAllowRestart() throws InterruptedException {
        loop.start();

        loop.stop(true);
        Thread.sleep(WAIT_TIME_MS);

        assertThrows(IllegalStateException.class, loop::start);
        assertEquals(0, loop.getProcessedEventsAmount().get());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void shouldNotAllowSubscriptionsAndEvents_inIllegalState(boolean startStop) throws InterruptedException {
        if(startStop) {
            loop.start();
            loop.stop(true);
            Thread.sleep(WAIT_TIME_MS);
        }

        Subscription subscription = new Subscription("test-subscription", "test-event", event -> {});
        assertThrows(IllegalStateException.class, () -> loop.subscribe(subscription));

        Event event = Event.create("test-event");
        assertThrows(IllegalStateException.class, () -> loop.submit(event));
    }

}