/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.profiles.scanners.ProfileScanner;

@ExtendWith(MockitoExtension.class)
class ProfileInitializationProcessTest {

    private ProfileInitializationProcess profileInitializationProcess;

    @Mock
    private ProfileScanner scannerA;

    @Mock
    private ProfileScanner scannerB;

    @Mock
    private ProfileCleaner cleaner;

    @Mock
    private ProfileValidator validator;

    @BeforeEach
    public void setUp() {
        profileInitializationProcess = new ProfileInitializationProcess(cleaner, validator);
    }

    @Test
    public void shouldInitializeProfiles_whenAllValid() {
        when(scannerA.scan()).thenReturn(Set.of("A", "b"));
        when(scannerB.scan()).thenReturn(Set.of("b", "C"));

        when(cleaner.clean("A")).thenReturn("a");
        when(cleaner.clean("b")).thenReturn("b");
        when(cleaner.clean("C")).thenReturn("c");

        doNothing().when(validator).validate("a");
        doNothing().when(validator).validate("b");
        doNothing().when(validator).validate("c");

        var input = List.of(scannerA, scannerB);
        ProfilesContainer profilesContainer = profileInitializationProcess.initializeProfiles(input);

        assertEquals(Set.of("a", "b", "c"), profilesContainer.profiles());
        assertTrue(profilesContainer.isProfileSet("a"));
        assertTrue(profilesContainer.isProfileSet("b"));
        assertTrue(profilesContainer.isProfileSet("c"));
    }

    @Test
    public void shouldFailInitialization_whenProfilesAreInvalid() {
        String invalidProfile = "invalid_profile!!!";
        when(scannerA.scan()).thenReturn(Set.of(invalidProfile));

        when(cleaner.clean(invalidProfile)).thenReturn(invalidProfile);

        doThrow(new InvalidProfileException(invalidProfile)).when(validator).validate(invalidProfile);

        var input = List.of(scannerA, scannerB);
        var exception = assertThrows(InvalidProfileException.class, () -> {
            profileInitializationProcess.initializeProfiles(input);
        });

        assertEquals(
                exception.getMessageTemplate().formatted(invalidProfile, ProfileValidator.class.getName()),
                exception.getMessage()
        );
    }

}
