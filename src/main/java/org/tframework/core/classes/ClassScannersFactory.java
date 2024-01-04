/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility methods to create various {@link ClassScanner}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassScannersFactory {

    /**
     * Creates a {@link PackageClassScanner}.
     * @param packages Packages to assign to the scanner.
     */
    public static PackageClassScanner createPackageClassScanner(Set<String> packages) {
        return new PackageClassScanner(packages);
    }

    /**
     * Creates a {@link PackageClassScanner} with no packages assigned. This can be done
     * later with {@link PackageClassScanner#setPackageNames(Set)}.
     */
    public static PackageClassScanner createPackageClassScanner() {
        return new PackageClassScanner();
    }

    /**
     * Creates a {@link NestedClassScanner}.
     * @param classToScan Class to assign to the scanner.
     */
    public static NestedClassScanner createNestedClassScanner(Class<?> classToScan) {
        return new NestedClassScanner(classToScan);
    }

}
