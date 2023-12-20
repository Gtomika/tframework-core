/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import java.util.List;

/**
 * The class scanner is responsible for finding classes. How and where the scan
 * looks is implementation dependent.
 */
public interface ClassScanner {

    /**
     * Find and collect the classes. This operation may take time, so the result should be
     * saved for future use instead of calling this again and again.
     * @return {@link List} of {@link Class} objects that this scanner found.
     */
    List<Class<?>> scanClasses();

}
