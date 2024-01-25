/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import lombok.Builder;
import org.tframework.core.TFrameworkInternal;

/**
 * Contains the data required by the core initializer.
 * @param applicationName The name of the application.
 * @param rootClass The root class of the application.
 * @param args The command line arguments.
 * @see CoreInitializationProcess
 */
@Builder
@TFrameworkInternal
public record CoreInitializationInput(
        String applicationName,
        Class<?> rootClass,
        String[] args
) {
}
