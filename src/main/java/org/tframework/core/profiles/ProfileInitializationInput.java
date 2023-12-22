/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.List;
import lombok.Builder;

/**
 * Contains all input required by the {@link ProfileInitializationProcess}.
 * @param profileScanners The {@link ProfileScanner}s to use during the initialization process.
 */
@Builder
public record ProfileInitializationInput(List<ProfileScanner> profileScanners) {
}
