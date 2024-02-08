/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.Application;
import org.tframework.core.TFrameworkRootClass;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.profiles.InvalidProfileException;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.reflection.annotations.AnnotationScanner;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TFrameworkRootClass
@ExtendWith(MockitoExtension.class)
class CoreInitializationProcessTest {

    private CoreInitializationProcess coreInitializationProcess;

    @Mock
    private ProfilesCoreInitializer profilesCoreInitializer;

    @Mock
    private PropertiesCoreInitializer propertiesCoreInitializer;

    @Mock
    private ElementsCoreInitializer elementsCoreInitializer;

    @Mock
    private AnnotationScanner annotationScanner;

    @BeforeEach
    public void setUp() {
        coreInitializationProcess = new CoreInitializationProcess(
                profilesCoreInitializer,
                propertiesCoreInitializer,
                elementsCoreInitializer,
                annotationScanner
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

        when(annotationScanner.scanOneStrict(expectedApp.getRootClass(), TFrameworkRootClass.class))
                .thenReturn(Optional.of(expectedApp.getRootClass().getAnnotation(TFrameworkRootClass.class)));
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
    public void shouldThrowInitializationException_whenRootClassIsNotCorrectlyAnnotated() {
        when(annotationScanner.scanOneStrict(CoreInitializationProcessTest.class, TFrameworkRootClass.class))
                .thenReturn(Optional.empty());

        CoreInitializationInput input = CoreInitializationInput.builder()
                .applicationName("testApp")
                .rootClass(CoreInitializationProcessTest.class)
                .args(new String[]{"testArg"})
                .build();

        var exception = assertThrows(InitializationException.class, () -> coreInitializationProcess.performCoreInitialization(input));

        String reason = CoreInitializationProcess.ROOT_CLASS_NOT_ANNOTATED_ERROR_TEMPLATE.formatted(CoreInitializationProcessTest.class.getName());
        assertEquals(
                exception.getMessageTemplate().formatted(reason),
                exception.getMessage()
        );
    }

    @Test
    public void shouldThrowInitializationException_whenACoreInitializerThrowsException() {
        when(annotationScanner.scanOneStrict(CoreInitializationProcessTest.class, TFrameworkRootClass.class))
                .thenReturn(Optional.of(CoreInitializationProcessTest.class.getAnnotation(TFrameworkRootClass.class)));
        var cause = new InvalidProfileException("invalid!");
        when(profilesCoreInitializer.initialize(any())).thenThrow(cause);

        CoreInitializationInput input = CoreInitializationInput.builder()
                .args(new String[]{"testArg"})
                .rootClass(CoreInitializationProcessTest.class)
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
