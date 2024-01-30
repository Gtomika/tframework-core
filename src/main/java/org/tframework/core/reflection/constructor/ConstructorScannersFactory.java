/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.constructor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Factory for {@link ConstructorScanner}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstructorScannersFactory {

    /**
     * Creates a {@link ConstructorScanner} that other framework components can use.
     */
    public static ConstructorScanner createDefaultConstructorScanner() {
        return new SimpleConstructorScanner();
    }

}
