/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import org.tframework.core.Application;
import org.tframework.core.TFrameworkInternal;

/**
 * The result of the core initialization process.
 * @param application An {@link Application} that was initialized.
 * @see CoreInitializationProcess
 */
@TFrameworkInternal
public record CoreInitializationResult(Application application) {
}
