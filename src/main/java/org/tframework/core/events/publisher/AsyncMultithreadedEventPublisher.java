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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.core.events.Event;
import org.tframework.core.events.Subscription;

/**
 * This {@link EventPublisher} uses several threads to publish events asynchronously. It is
 * the recommended publisher, because it does not block the event management from handling other events.
 * The {@value #EVENT_PUBLISHER_THREAD_POOL_SIZE_PROPERTY} property can be used to configure the number of threads
 * used by this publisher. The default is 3.
 */
@Slf4j
@Element
public class AsyncMultithreadedEventPublisher implements EventPublisher {

    public static final String EVENT_PUBLISHER_THREAD_POOL_SIZE_PROPERTY =
            "org.tframework.core.events.thread-pool-size";

    private final ExecutorService executorService;

    public AsyncMultithreadedEventPublisher(
            @InjectProperty(value = EVENT_PUBLISHER_THREAD_POOL_SIZE_PROPERTY, defaultValue = "3") int threadPoolSize
    ) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
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
