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
package org.tframework.core.elements.context;

import org.tframework.core.TFrameworkInternal;

/**
 * Internal data holder used when element instances are requested.
 * @param instance The requested instance of the element.
 * @param reused True if the instance was not newly created, but reused. For example,
 *               a singleton element context will reuse the existing instance.
 */
@TFrameworkInternal
public record InstanceRequest(
        Object instance,
        boolean reused
) {

    public static InstanceRequest ofReused(Object instance) {
        return new InstanceRequest(instance, true);
    }

    public static InstanceRequest ofNewlyCreated(Object instance) {
        return new InstanceRequest(instance, false);
    }

}
