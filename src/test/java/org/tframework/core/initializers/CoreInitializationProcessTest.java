/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.Application;
import org.tframework.core.profiles.InvalidProfileException;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainer;

@ExtendWith(MockitoExtension.class)
class CoreInitializationProcessTest {

    private CoreInitializationProcess coreInitializationProcess;

    @Mock
    private ProfilesCoreInitializer profilesCoreInitializer;

    @Mock
    private PropertiesCoreInitializer propertiesCoreInitializer;

    @BeforeEach
    public void setUp() {
        coreInitializationProcess = new CoreInitializationProcess(
                profilesCoreInitializer,
                propertiesCoreInitializer
        );
    }

    @Test
    public void shouldPerformCoreInitialization() {
        var expectedApp = Application.builder()
                .profilesContainer(ProfilesContainer.fromProfiles(Set.of("a, b")))
                .propertiesContainer(PropertiesContainer.empty())
                .build();

        when(profilesCoreInitializer.initialize(any())).thenReturn(expectedApp.profilesContainer());
        when(propertiesCoreInitializer.initialize(any())).thenReturn(expectedApp.propertiesContainer());

        CoreInitializationInput input = CoreInitializationInput.builder()
                .args(new String[]{"testArg"})
                .build();

        var actualApp = coreInitializationProcess.performCoreInitialization(input);

        assertEquals(expectedApp, actualApp);
    }

    @Test
    public void shouldThrowInitializationException_whenACoreInitializerThrowsException() {
        var cause = new InvalidProfileException("invalid!");
        when(profilesCoreInitializer.initialize(any()))
                .thenThrow(cause);

        CoreInitializationInput input = CoreInitializationInput.builder()
                .args(new String[]{"testArg"})
                .build();

        var exception = assertThrows(InitializationException.class, () -> {
            coreInitializationProcess.performCoreInitialization(input);
        });

        assertEquals(
                exception.getMessageTemplate().formatted(cause.getClass().getName()),
                exception.getMessage()
        );
        assertEquals(
                cause.getMessage(),
                exception.getCause().getMessage()
        );
    }

}
