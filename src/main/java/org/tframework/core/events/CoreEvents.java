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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.Application;

/**
 * Defines topics for events produced by the TFramework core module.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CoreEvents {

    /**
     * Event topic for the application initialization event. The payload of this event
     * will be the {@link Application} instance. Only a single event will be sent on
     * this topic, when the application is initialized.
     */
    public static final String APPLICATION_INITIALIZED = "org-tframework-core-application-initialized";

    /**
     * Event topic for the application shutting down event. The payload of this event
     * will be the {@link Application} instance. Only a single event will be sent on
     *  this topic, when the application is shutting down.
     */
    public static final String APPLICATION_SHUTTING_DOWN = "org-tframework-core-application-shutting-down";

}
