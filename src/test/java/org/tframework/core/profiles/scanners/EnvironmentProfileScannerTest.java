/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles.scanners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.tframework.core.profiles.scanners.EnvironmentProfileScanner.TFRAMEWORK_PROFILES_VARIABLE_NAME;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.readers.EnvironmentVariableNotFoundException;
import org.tframework.core.readers.EnvironmentVariableReader;

@ExtendWith(MockitoExtension.class)
class EnvironmentProfileScannerTest {

    @Mock
    private EnvironmentVariableReader variableReader;

    @Test
    public void shouldReturnProfilesList_whenProfilesVariableIsSet() {
        when(variableReader.readVariable(TFRAMEWORK_PROFILES_VARIABLE_NAME))
                .thenReturn("p1,p2");

        EnvironmentProfileScanner scanner = new EnvironmentProfileScanner(variableReader);
        Set<String> profiles = scanner.scan();

        assertEquals(Set.of("p1", "p2"), profiles);
    }

    @Test
    public void shouldReturnEmptyList_whenProfileVariableIsNotSet() {
        when(variableReader.readVariable(TFRAMEWORK_PROFILES_VARIABLE_NAME))
                .thenThrow(new EnvironmentVariableNotFoundException(TFRAMEWORK_PROFILES_VARIABLE_NAME));

        EnvironmentProfileScanner scanner = new EnvironmentProfileScanner(variableReader);
        Set<String> profiles = scanner.scan();

        assertEquals(Set.of(), profiles);
    }

}
