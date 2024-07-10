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
package org.tframework.core.elements;

/**
 * Enum for all supported element scopes. The element scope defines the lifecycle of an element.
 */
public enum ElementScope {

    /**
     * The element will be created once at initialization, and will be reused for all injections.
     */
    SINGLETON,

    /**
     * The element will be created once for each injection, only when it is needed.
     */
    PROTOTYPE;

}
