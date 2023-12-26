/* Licensed under Apache-2.0 2023. */
package readers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.tframework.core.readers.ReadersFactory;

class ReadersFactoryTest {

    @Test
    public void shouldCreateEnvironmentVariableReader() {
        var reader = ReadersFactory.createEnvironmentVariableReader();
        assertNotNull(reader);
    }

}
