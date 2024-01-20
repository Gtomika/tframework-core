/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.scanner;

import java.util.List;
import lombok.Builder;

/**
 * A collection of all kind of {@link org.tframework.core.elements.scanner.ElementScanner}s that will be used
 * by the framework to detect elements.
 * @param elementClassScanners Scanners that will find element classes.
 * @param elementMethodScanners Scanners that will find element methods.
 */
@Builder
public record ElementScannersBundle(
        List<ElementClassScanner> elementClassScanners,
        List<ElementMethodScanner> elementMethodScanners
) {
}
