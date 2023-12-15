/* Licensed under Apache-2.0 2023. */
package org.tframework.core.loop;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SingleThreadedEventLoop implements EventLoop {

    @Getter
    private final String name;
    private final Thread loopThread;

    private final AtomicBoolean keepAlive;
    private final AtomicBoolean showdownCompleted;
    private final AtomicBoolean processAllEventsBeforeShutdown;

    @Getter
    private final AtomicInteger processedEventsAmount;

    private final BlockingQueue<Event> eventQueue;
    private final Dispatcher dispatcher;
    private final CopyOnWriteArrayList<Subscription> subscriptionRequests;

    public SingleThreadedEventLoop(String name, Dispatcher dispatcher) {
        this.name = name;
        this.dispatcher = dispatcher;

        keepAlive = new AtomicBoolean(false);
        showdownCompleted = new AtomicBoolean(false);
        processAllEventsBeforeShutdown = new AtomicBoolean(false);
        processedEventsAmount = new AtomicInteger(0);

        eventQueue = new LinkedBlockingQueue<>();
        subscriptionRequests = new CopyOnWriteArrayList<>();

        loopThread = new Thread(this::loopRunnable);
        loopThread.setName(name);
        loopThread.setDaemon(false);
    }

    private void loopRunnable() {
        while (keepAlive.get() || (processAllEventsBeforeShutdown.get() && !eventQueue.isEmpty())) {
            try {
                subscriptionRequests.forEach(subscription -> {
                    dispatcher.registerSubscription(subscription);
                    log.trace("New subscription request for event '{}' is registered to the Dispatcher", subscription.eventName());
                });
                subscriptionRequests.clear();

                Event event = eventQueue.take();
                log.trace("Event loop found an event in the queue. Event name: '{}'", event.name());
                processedEventsAmount.incrementAndGet();

                dispatcher.dispatchEvent(event);
            } catch (Throwable t) {
                log.error("Exception in the event loop.", t);
            }
        }
        dispatcher.showdown();
    }

    @Override
    public void start() {
        if(readyToStart()) {
            keepAlive.set(true);
            loopThread.start();
            log.info("The event loop has started.");
        } else {
            throw new IllegalStateException("The event loop cannot be started because it is already running, or has been stopped before.");
        }
    }

    @Override
    public void stop(boolean force) {
        keepAlive.set(false);
        if(force) {
            log.info("The event loop has been signalled to stop as soon as possible.");
            processAllEventsBeforeShutdown.set(false);
            loopThread.interrupt(); //to interrupt eventQueue.take() method
        } else {
            log.info("The event loop has been signalled to stop after processing submitted events.");
            processAllEventsBeforeShutdown.set(true);
        }
        showdownCompleted.set(true);
    }

    @Override
    public void submit(Event event) {
        if(readyToAcceptInput()) {
            eventQueue.offer(event);
        } else {
            throw new IllegalStateException("Events cannot be submitted when the loop is not alive.");
        }
    }

    @Override
    public void subscribe(Subscription subscription) {
        if(readyToAcceptInput()) {
            subscriptionRequests.add(subscription);
        } else {
            throw new IllegalStateException("Subscriptions cannot be made when the loop is not alive.");
        }
    }

    private boolean readyToStart() {
        return !keepAlive.get() && !showdownCompleted.get();
    }

    private boolean readyToAcceptInput() {
        return keepAlive.get();
    }
}
