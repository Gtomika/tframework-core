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
package org.tframework.core.properties.filescanners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PropertyFileScannerTest {

    @Mock
    private PropertyFileScanner scanner1;

    @Mock
    private PropertyFileScanner scanner2;

    @Test
    public void shouldMergeScanners() {
        when(scanner1.scan()).thenReturn(List.of("file1.yaml", "file2.yaml"));
        when(scanner2.scan()).thenReturn(List.of("file3.yaml", "file4.yaml"));
        List<String> propertyFiles = PropertyFileScanner.merging(List.of(scanner1, scanner2));

        assertEquals(List.of("file1.yaml", "file2.yaml", "file3.yaml", "file4.yaml"), propertyFiles);
    }

}
