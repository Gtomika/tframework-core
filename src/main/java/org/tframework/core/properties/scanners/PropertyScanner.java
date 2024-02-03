/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.scanners;

import java.util.List;

/**
 * Property scanners find raw property name-value pairs. It is not responsible for further processing
 * or validating the scanned raw values.
 */
public interface PropertyScanner {

    /**
     * Finds a list of raw properties.
     */
    List<String> scanProperties();

}
