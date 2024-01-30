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
import org.tframework.core.elements.ElementsContainer;
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

    @Mock
    private ElementsCoreInitializer elementsCoreInitializer;

    @BeforeEach
    public void setUp() {
        coreInitializationProcess = new CoreInitializationProcess(
                profilesCoreInitializer,
                propertiesCoreInitializer,
                elementsCoreInitializer
        );
    }

    @Test
    public void shouldPerformCoreInitialization() {
        var expectedApp = Application.builder()
                .name("testApp")
                .rootClass(CoreInitializationProcessTest.class)
                .profilesContainer(ProfilesContainer.fromProfiles(Set.of("a, b")))
                .propertiesContainer(PropertiesContainer.empty())
                .elementsContainer(ElementsContainer.empty())
                .build();
        expectedApp.finalizeApplication();

        when(profilesCoreInitializer.initialize(any())).thenReturn(expectedApp.getProfilesContainer());
        when(propertiesCoreInitializer.initialize(any())).thenReturn(expectedApp.getPropertiesContainer());
        when(elementsCoreInitializer.initialize(any())).thenReturn(expectedApp.getElementsContainer());

        CoreInitializationInput input = CoreInitializationInput.builder()
                .applicationName("testApp")
                .rootClass(CoreInitializationProcessTest.class)
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
