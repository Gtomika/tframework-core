/*
Copyright 2024 Tamas Gaspar

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
package org.tframework.core.properties.placeholders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.readers.EnvironmentVariableNotFoundException;
import org.tframework.core.readers.EnvironmentVariableReader;

@ExtendWith(MockitoExtension.class)
public class EnvironmentPlaceholderResolverTest {

    private static final String VAR_NAME = "VAR_NAME";
    private static final String VAR_VALUE = "VAR_VALUE";
    private static final String VAR_DEFAULT_VALUE = "VAR_DEFAULT_VALUE";

    @Mock
    private EnvironmentVariableReader environmentVariableReader;

    @Mock
    private PropertiesContainer propertiesContainer;

    private EnvironmentPlaceholderResolver resolver;

    @BeforeEach
    public void setUp() {
        resolver = new EnvironmentPlaceholderResolver(environmentVariableReader);
    }

    @Test
    public void shouldNotResolveAnything_whenNoPlaceholders() {
        String original = "abcd12345";
        String resolved = resolver.resolvePlaceholders(original, propertiesContainer);
        assertEquals(original, resolved);
    }

    @Test
    public void shouldResolvePlaceholder_whenVariableExists() {
        when(environmentVariableReader.readVariable(VAR_NAME)).thenReturn(VAR_VALUE);
        String original = "nice env{" + VAR_NAME + "}";

        String resolved = resolver.resolvePlaceholders(original, propertiesContainer);
        assertEquals("nice " + VAR_VALUE, resolved);
    }

    @Test
    public void shouldThrowException_whenVariableDoesNotExist() {
        when(environmentVariableReader.readVariable(VAR_NAME))
                .thenThrow(new EnvironmentVariableNotFoundException(VAR_NAME));
        String original = "nice env{" + VAR_NAME + "}";

        assertThrows(EnvironmentVariableNotFoundException.class,
                () -> resolver.resolvePlaceholders(original, propertiesContainer));
    }

    @Test
    public void shouldResolvePlaceholder_whenVariableDoesNotExists_andDefaultValueProvided() {
        when(environmentVariableReader.readVariable(VAR_NAME))
                .thenThrow(new EnvironmentVariableNotFoundException(VAR_NAME));
        String original = "nice env{" + VAR_NAME + "|" + VAR_DEFAULT_VALUE + "}";

        String resolved = resolver.resolvePlaceholders(original, propertiesContainer);
        assertEquals("nice " + VAR_DEFAULT_VALUE, resolved);
    }

    @Test
    public void shouldResolveMultiplePlaceholders() {
        when(environmentVariableReader.readVariable(VAR_NAME)).thenReturn(VAR_VALUE);
        String original = "nice env{" + VAR_NAME + "} and env{" + VAR_NAME + "}";

        String resolved = resolver.resolvePlaceholders(original, propertiesContainer);
        assertEquals("nice " + VAR_VALUE + " and " + VAR_VALUE, resolved);
    }
}
