/* Licensed under Apache-2.0 2023. */
package org.tframework.core.loop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.MultiValueMap;

@Slf4j
public class AsynchronousDispatcher implements Dispatcher {

    private final ExecutorService dispatchExecutor;
    private final MultiValueMap<String, Subscription> subscriptions;

    public AsynchronousDispatcher() {
        dispatchExecutor = Executors.newCachedThreadPool();
        subscriptions = new MultiValueMap<>();
    }

    @Override
    public void registerSubscription(Subscription subscription) {
        subscriptions.put(subscription.eventName(), subscription);
        log.trace("New subscription '{}' has been registered.", subscription.subscriptionName());
    }

    @Override
    public void dispatchEvent(Event event) {
        var eventSubscriptions = subscriptions.getCollection(event.name());
        if(eventSubscriptions != null && !eventSubscriptions.isEmpty()) {
            eventSubscriptions.forEach(subscription -> {
                log.trace("Dispatching event '{}' to subscription '{}'", event.name(), subscription.subscriptionName());
                dispatchExecutor.submit(() -> subscription.callback().accept(event));
            });
        } else {
            log.trace("No subscriptions found for event '{}', it will not be dispatched.", event.name());
        }
    }

    @Override
    public void showdown() {
        dispatchExecutor.shutdown();
        log.trace("The dispatcher has been shut down (already dispatched events will finish).");
    }

}
