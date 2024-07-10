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
package org.tframework.core.elements.scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.PropertiesContainerFactory;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;
import org.tframework.core.reflection.classes.ClassFiltersFactory;
import org.tframework.core.reflection.classes.PackageClassScanner;

@ExtendWith(MockitoExtension.class)
class InternalElementClassScannerTest {

    @Mock
    private PackageClassScanner packageClassScanner;

    private InternalElementClassScanner internalElementClassScanner;

    @BeforeEach
    public void setUp() {
        PropertiesContainer propertiesContainer = PropertiesContainerFactory.empty();
        //To reduce mocking noise, some of the classes are not mocked.
        internalElementClassScanner = InternalElementClassScanner.builder()
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .classFilter(ClassFiltersFactory.createDefaultClassFilter())
                .classScanner(packageClassScanner)
                .propertiesContainer(propertiesContainer)
                .build();
    }

    @Test
    public void shouldScanInternalElements() {
        when(packageClassScanner.scanClasses()).thenReturn(Set.of(SomeElement.class, SomeNonElement.class));

        var results = internalElementClassScanner.scanElements();

        assertEquals(1, results.size());
        assertTrue(results.stream().anyMatch(r -> r.annotationSource().equals(SomeElement.class)));
    }

    @Element
    static class SomeElement {}

    static class SomeNonElement {}

}
