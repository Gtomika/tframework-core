/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileMergerTest {

    @Mock
    private ProfileScanner scannerA;

    @Mock
    private ProfileScanner scannerB;

    @Test
    public void shouldMergeProfiles() {
        when(scannerA.scan()).thenReturn(Set.of("test", "demo"));
        when(scannerB.scan()).thenReturn(Set.of("test", "db"));

        Set<String> mergedProfiles = ProfileMerger.merging(List.of(scannerA, scannerB))
                .mergeAndStream()
                .collect(Collectors.toSet());

        assertEquals(Set.of("test", "demo", "db"), mergedProfiles);
    }

}