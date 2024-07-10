/*
Copyright 2023 Tamas Gaspar

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
package org.tframework.core.initializers;

import org.tframework.core.TFrameworkInternal;

/**
 * The core initializers define tasks that must be run before all else during application startup, and are
 * required for the framework to function. This is an internal class. If you need to register an initializer for
 * the application, use {@link CustomInitializer} instead.
 * @param <Input> Type of the input required by this initializer.
 * @param <Output> Type of the output that this initializer produces.
 * @see CustomInitializer
 * @see CoreInitializationFactory
 */
@TFrameworkInternal
public interface CoreInitializer<Input, Output> {

    /**
     * The method that will be executed during core initialization. Any type of exception can be thrown from the
     * initializer. An exception here means that the initialization process failed, and the application cannot start.
     */
    Output initialize(Input input);

}
