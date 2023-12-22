/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileInitializationProcessTest {

    private final ProfileInitializationProcess profileInitializationProcess = new ProfileInitializationProcess();

    @Mock
    private ProfileScanner scannerA;

    @Mock
    private ProfileScanner scannerB;

    @Test
    public void shouldInitializeProfiles_whenAllValid() {
        when(scannerA.scan()).thenReturn(Set.of("A", "b"));
        when(scannerB.scan()).thenReturn(Set.of("b", "C"));

        var input = ProfileInitializationInput.builder()
                .profileScanners(List.of(scannerA, scannerB))
                .build();

        Profiles profiles = profileInitializationProcess.initializeProfiles(input);

        assertEquals(Set.of("a", "b", "c"), profiles.profiles());
        assertTrue(profiles.hasProfile("a"));
        assertTrue(profiles.hasProfile("b"));
        assertTrue(profiles.hasProfile("c"));
    }

    @Test
    public void shouldFailInitialization_whenProfilesAreInvalid() {
        String invalidProfile = "invalid_profile!!!";
        when(scannerA.scan()).thenReturn(Set.of("A", "b"));
        when(scannerB.scan()).thenReturn(Set.of(invalidProfile, "C"));

        var input = ProfileInitializationInput.builder()
                .profileScanners(List.of(scannerA, scannerB))
                .build();

        var exception = assertThrows(InvalidProfileException.class, () -> {
            profileInitializationProcess.initializeProfiles(input);
        });

        assertEquals(
                exception.getMessageTemplate().formatted(invalidProfile, ProfileValidator.class.getName()),
                exception.getMessage()
        );
    }

}