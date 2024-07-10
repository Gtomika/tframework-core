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

/**
 * Represents an event that can be published to subscribers of a topic.
 * @param topic The topic to which this event should be published.
 * @param payload The payload of the event.
 * @see EventManager
 */
public record Event(
        String topic,
        Object payload
) {
}
