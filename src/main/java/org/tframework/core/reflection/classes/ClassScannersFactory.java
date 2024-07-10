/*
Copyright 2023 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core.reflection.classes;

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
     * later with {@link PackageClassScanner#setPackageNames(Set)} and {@link PackageClassScanner#setRejectedPackages(Set)}.
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
