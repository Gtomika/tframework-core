/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.scanners;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.NonNull;

/**
 * The property file scanner finds property files that will be used by the framework.
 * These files must be in the {@code resources} folder. A scanner may return multiple unique files.
 * @see PropertyFileScannersFactory
 */
public interface PropertyFileScanner {

    /**
     * Scans for property files.
     * @return The set of property files found. These paths are relative to the {@code resources} folder.
     */
    Set<String> scan();

    /**
     * Combines the results of multiple scanners into a single set of unique property files.
     * @param scanners List of scanners to use, must not be null. The combines property files will be
     *                 returned in the order of these scanners.
     */
    static Set<String> merging(@NonNull List<PropertyFileScanner> scanners) {
        LinkedHashSet<String> propertyFiles = new LinkedHashSet<>();
        for(PropertyFileScanner scanner : scanners)
            propertyFiles.addAll(scanner.scan());
        return propertyFiles;
    }

}
