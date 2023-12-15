/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import org.tframework.core.TFrameworkInternal;

/**
 * The core initializers define tasks that must be run before all else during application startup, and are
 * required for the framework to function.
 * <p>
 * This is an internal class. If you need to register an initializer for your application, use {@link Initializer}
 * instead.
 * @see Initializer
 */
@TFrameworkInternal
public interface CoreInitializer {

    /**
     * The method that will be executed during core initialization.
     * @throws Exception Any type of exception can be thrown from the initializer. An exception here means that
     * the initialization process failed, and the application cannot start.
     */
    void initialize() throws Exception;

}
