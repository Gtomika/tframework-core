/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Utilities to construct {@link MethodScanner}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MethodScannersFactory {

    /**
     * Creates a {@link MethodScanner} that scans the given class.
     */
    public static MethodScanner createDefaultMethodScanner(@NonNull Class<?> classToScan) {
        return new DefaultMethodScanner(classToScan);
    }

}
