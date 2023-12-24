/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

/**
 * Initializers define tasks are run at application startup.
 */
public interface CustomInitializer {

    /**
     * The method that will be executed at application startup. Any type of runtime exception can be thrown
     * from the initializer. An exception here means that the initialization process failed, and the
     * application cannot start.
     */
    void initialize();

}
