/*
Copyright 2023 Tamas Gaspar

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
import org.tframework.core.profiles.scanners.ProfileScanner;

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
