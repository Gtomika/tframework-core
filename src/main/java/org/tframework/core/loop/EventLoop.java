/* Licensed under Apache-2.0 2023. */
package org.tframework.core.loop;

public interface EventLoop {

    void start();

    void stop(boolean force);

    void submit(Event event);

    void subscribe(Subscription subscription);
}
