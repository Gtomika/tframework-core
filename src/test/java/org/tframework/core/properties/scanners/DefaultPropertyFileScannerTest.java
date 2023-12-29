/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.scanners;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import org.junit.jupiter.api.Test;

class DefaultPropertyFileScannerTest {

    @Test
    public void testScan() {
        DefaultPropertyFileScanner defaultPropertyFileScanner = new DefaultPropertyFileScanner();
        var files = defaultPropertyFileScanner.scan();
        assertEquals(Set.of(DefaultPropertyFileScanner.DEFAULT_PROPERTY_FILE_NAME), files);
    }

}
