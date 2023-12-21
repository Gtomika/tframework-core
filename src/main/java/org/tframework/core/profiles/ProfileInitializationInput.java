/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

/**
 * Contains all input required by the {@link ProfileInitializationProcess}.
 * @param args The command line arguments, as received in the {@code main} method.
 */
public record ProfileInitializationInput(String[] args) {
}
