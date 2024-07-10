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
package org.tframework.core.events;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Represents a subscription to a topic.
 * @param topic The topic that this subscription is for.
 * @param subscriptionId The ID of this subscription, used for further operations, such as unsubscribing.
 * @param callback The callback to be invoked when an event is published to the topic.
 * @see EventManager
 */
public record Subscription(
        String topic,
        UUID subscriptionId,
        Consumer<Object> callback
) {
}
