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
package org.tframework.core.properties.filescanners;

import java.util.ArrayList;
import java.util.List;
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
    List<String> scan();

    /**
     * An informative string from where this file scanner finds the property files.
     */
    String sourceName();

    /**
     * Combines the results of multiple scanners into a single set of unique property files.
     * @param scanners List of scanners to use, must not be null. The combines property files will be
     *                 returned in the order of these scanners.
     */
    static List<String> merging(@NonNull List<PropertyFileScanner> scanners) {
        List<String> propertyFiles = new ArrayList<>();
        for(PropertyFileScanner scanner : scanners)
            propertyFiles.addAll(scanner.scan());
        return propertyFiles;
    }

}
