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

import org.tframework.core.events.Event;
import org.tframework.core.events.Subscription;

/**
 * This {@link EventPublisher} simply invokes the callback of the subscription and
 * waits until it's completed. It is the most basic implementation of an event publisher. It has the
 * danger that if a callback takes too long to complete, it will block the event management from
 * handling other events.
 */
public class SimpleEventPublisher implements EventPublisher {

    @Override
    public void publish(Event event, Subscription subscription) {
        subscription.callback().accept(event.payload());
    }
}
