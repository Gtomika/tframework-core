/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utilities to construct {@link MethodScanner}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MethodScannersFactory {

    /**
     * Creates a {@link MethodScanner} that other framework components can use.
     */
    public static MethodScanner createDefaultMethodScanner() {
        return new DeclaredMethodScanner();
    }

}
