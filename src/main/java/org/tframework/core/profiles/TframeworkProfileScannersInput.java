/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import org.tframework.core.TFrameworkInternal;

/**
 * Contains all data required to create the frameworks default set of {@link ProfileScanner}s.
 * @param args Command line arguments.
 */
@TFrameworkInternal
public record TframeworkProfileScannersInput(String[] args) {
}
