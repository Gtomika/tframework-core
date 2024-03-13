/* Licensed under Apache-2.0 2024. */
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
