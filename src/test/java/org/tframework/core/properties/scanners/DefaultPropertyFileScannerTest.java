/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.scanners;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class DefaultPropertyFileScannerTest {

    @Test
    public void testScan() {
        DefaultPropertyFileScanner defaultPropertyFileScanner = new DefaultPropertyFileScanner();
        List<String> files = defaultPropertyFileScanner.scan();
        assertEquals(List.of(DefaultPropertyFileScanner.DEFAULT_PROPERTY_FILE_NAME), files);
    }

}
