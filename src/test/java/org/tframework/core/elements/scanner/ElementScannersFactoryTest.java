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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.elements.ElementsInitializationInput;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainerFactory;

public class ElementScannersFactoryTest {

    private final ElementsInitializationInput input = ElementsInitializationInput.builder()
            .application(Application.builder()
                    .propertiesContainer(PropertiesContainerFactory.empty())
                    .profilesContainer(ProfilesContainer.empty())
                    .build())
            .rootClass(this.getClass())
            .preConstructedElementData(Set.of())
            .build();

    @Test
    public void shouldCreateDefaultElementScannersBundle() {
        var scannersBundle = ElementScannersFactory.createDefaultElementScannersBundle(input);

        assertEquals(4, scannersBundle.elementClassScanners().size());
        assertEquals(1, scannersBundle.elementMethodScanners().size());
    }

    @Test
    public void shouldCreateDefaultElementClassScanners() {
        var scanners = ElementScannersFactory.createDefaultElementClassScanners(input);

        assertEquals(4, scanners.size());
        assertInstanceOf(RootElementClassScanner.class, scanners.get(0));
        assertInstanceOf(InternalElementClassScanner.class, scanners.get(1));
        assertInstanceOf(PackagesElementClassScanner.class, scanners.get(2));
        assertInstanceOf(ClassesElementClassScanner.class, scanners.get(3));
    }

    @Test
    public void shouldCreateDefaultElementMethodScanners() {
        var scanners = ElementScannersFactory.createDefaultElementMethodScanners(input);

        assertEquals(1, scanners.size());
        assertInstanceOf(FixedClassesElementMethodScanner.class, scanners.getFirst());
    }

}
