/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers.core;

import org.tframework.core.Application;

/**
 * The result of the core initialization process.
 * @param application An {@link Application} that was initialized.
 * @see CoreInitializationProcess
 */
public record CoreInitializationResult(Application application) {
}
