/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.scanners;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyScannersFactory {

    /**
     * Creates the default {@link PropertyScanner}s that the framework will use to
     * find directly specified properties.
     */
    public static List<PropertyScanner> createDefaultPropertyScanners() {
        return List.of();
    }

}
