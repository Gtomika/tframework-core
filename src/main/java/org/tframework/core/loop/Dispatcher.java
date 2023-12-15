/* Licensed under Apache-2.0 2023. */
package org.tframework.core.loop;

public interface Dispatcher {

    void registerSubscription(Subscription subscription);

    void dispatchEvent(Event event);

    void showdown();

}
