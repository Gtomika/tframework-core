/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import org.tframework.core.TFrameworkInternal;

/**
 * The core initializers define tasks that must be run before all else during application startup, and are
 * required for the framework to function. This is an internal class. If you need to register an initializer for
 * the application, use {@link CustomInitializer} instead.
 * @param <Input> Type of the input required by this initializer.
 * @param <Output> Type of the output that this initializer produces.
 * @see CustomInitializer
 */
@TFrameworkInternal
public interface CoreInitializer<Input, Output> {

    /**
     * The method that will be executed during core initialization. Any type of exception can be thrown from the
     * initializer. An exception here means that the initialization process failed, and the application cannot start.
     */
    Output initialize(Input input);

}
