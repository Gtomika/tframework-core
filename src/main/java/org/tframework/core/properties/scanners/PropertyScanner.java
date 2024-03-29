/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.scanners;

import java.util.List;

/**
 * Property scanners find raw property name-value pairs. It is not responsible for further processing
 * or validating the scanned raw values and usually passes on raw properties.
 * to a {@link org.tframework.core.properties.parsers.PropertyParser}.
 */
public interface PropertyScanner {

    /**
     * Finds a list of raw properties.
     */
    List<String> scanProperties();

    /**
     * An informative name from where this scanner looks up properties.
     */
    String sourceName();

}
