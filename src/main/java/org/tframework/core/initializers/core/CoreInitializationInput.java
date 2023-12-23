/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers.core;

import lombok.Builder;

/**
 * Contains the data required by the core initializer
 * @param args The command line arguments.
 * @see CoreInitializationProcess
 */
@Builder
public record CoreInitializationInput(String[] args) {
}
