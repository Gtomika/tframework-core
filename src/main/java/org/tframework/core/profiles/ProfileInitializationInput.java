/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import lombok.Builder;

/**
 * Contains all input need to construct {@link ProfileScanner}s.
 * @param args Command line arguments.
 */
@Builder
public record ProfileInitializationInput(String[] args) {
}
