/* Licensed under Apache-2.0 2024. */
package org.tframework.core.events.publisher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.events.Event;
import org.tframework.core.events.Subscription;

/**
 * This {@link EventPublisher} uses virtual threads to publish events asynchronously. It is
 * the recommended publisher, because virtual threads can scale well and this publisher does not
 * block the event management from handling other events.
 */
@Slf4j
public class AsyncVirtualEventPublisher implements EventPublisher {

    private final ExecutorService executorService;

    AsyncVirtualEventPublisher() {
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Override
    public void publish(Event event, Subscription subscription) {
        executorService.submit(() -> subscription.callback().accept(event.payload()));
    }

    /**
     * Instruct this event publisher to shutdown its executor service and wait for all
     * publishes to complete.
     * @param millis The maximum time to wait for the executor service to shutdown in milliseconds.
     */
    public void shutdown(long millis) {
        try {
            executorService.shutdown();
            executorService.awaitTermination(millis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for executor service to shutdown", e);
        }
    }
}
